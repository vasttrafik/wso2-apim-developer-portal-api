package org.vasttrafik.wso2.carbon.apimgt.portal.api.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Authorization;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.annotations.Status;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Series;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.beans.Statistic;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
//import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.ResourceBundleAware;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.StatisticsUtil;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.StatisticsUtil.Grouping;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.StatisticsUtil.Period;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.StatisticsUtil.Source;
import org.vasttrafik.wso2.carbon.apimgt.portal.api.utils.StatisticsUtil.Type;
import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("statistics")
public class Statistics extends PortalResource implements ResourceBundleAware {

	private static Log logger = LogFactory.getLog(Statistics.class);

	@Context
	private SecurityContext securityContext;

	private static DataSource ds;

	static {

		try {
			// Lookup DataSource from the pool
			javax.naming.Context initCtx = new InitialContext();
			javax.naming.Context initialContext = (javax.naming.Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) initialContext.lookup("jdbc/WSO2API");
		} catch (Exception e) {
			logger.error("Problem looking up datasource jdbc/WSO2API", e);
		}
	}

	@Status(Status.OK)
	@GET
	public List<Statistic> getStatistics(@QueryParam("source") final Source source,
			@QueryParam("period") @DefaultValue("week") final Period period,
			@QueryParam("grouping") @DefaultValue("day") final Grouping grouping,
			@QueryParam("type") final List<Type> type, 
			@HeaderParam("X-JWT-Assertion") final String authorization) 
	{
		if (Source.apis.equals(source)) {
			return getApiStatistics(type, period, grouping, "%", "%");
		} else {
			final String userName;

			try {
				authorize(authorization);
				userName = getEndUserName();
				//userName = Security.validateToken(authorization);
			} catch (Exception e) {
				throw new NotAuthorizedException(
						ResponseUtils.notAuthorizedError(resourceBundle, 2003L, new Object[][] {}));
			}
			return getApplicationStatistics(type, userName, "%");
		}
	}

	@Authorization
	@Status(Status.OK)
	@GET
	@Path("{applicationName}")
	public List<Statistic> getApplicationStatistics(
			@PathParam("applicationName") final String applicationName,
			@QueryParam("type") final List<Type> type) 
	{
		final String userName = securityContext.getUserPrincipal().getName();
		return getApplicationStatistics(type, userName, applicationName);
	}

	@Status(Status.OK)
	@GET
	@Path("{apiName}/{apiVersion}")
	public List<Statistic> getApiStatistics(@PathParam("apiName") final String apiName,
			@PathParam("apiVersion") final String apiVersion,
			@QueryParam("period") @DefaultValue("week") final Period period,
			@QueryParam("grouping") @DefaultValue("day") final Grouping grouping,
			@QueryParam("type") final List<Type> type)
	{
		return getApiStatistics(type, period, grouping, apiName, apiVersion);
	}
	
	@Authorization
	@Status(Status.OK)
	@DELETE
	public boolean deleteStatistics() 
	{
		final String userName = securityContext.getUserPrincipal().getName();
		return deleteStatistics(userName);
	}
	
	private boolean deleteStatistics(String userName) {

		if (userName.isEmpty() || userName == null || userName.equals("admin"))
			throw new BadRequestException(ResponseUtils.badRequest(resourceBundle, 18003L, new Object[][] {}));

		// Allocate and use a connection from the pool
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			con.setAutoCommit(false);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
			
			String userDate = "deleteduser-" + formatter.format(new Date());

			for (String statement : StatisticsUtil.SQL_DELETE_STATEMENT_LIST) {

				stmt = con.prepareStatement(statement);
				stmt.setString(1, userDate);
				stmt.setString(2, userName);
			}

			con.commit();

		} catch (Exception exception) {

			try {
				con.rollback();
			} catch (SQLException e) {
				logger.error("Problem rolling back transaction ", exception);
			}

			logger.error("Error: ", exception);
			exception.printStackTrace();
			throw new InternalServerErrorException(ResponseUtils.serverError(exception));
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		
		return true;
	}

	private List<Statistic> getApiStatistics(List<Type> types, Period period, Grouping grouping, String apiName,
			String apiVersion) {

		ArrayList<Statistic> statisticList = new ArrayList<Statistic>();

		// Allocate and use a connection from the pool
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();

			for (Type type : types) {

				Statistic statistic = null;

				stmt = con
						.prepareStatement(StatisticsUtil.SQL_API_STATEMENT_MAP.get(type.name() + period));
				stmt.setString(1, apiName);
				stmt.setString(2, apiVersion);

				rs = stmt.executeQuery();

				if (type.equals(Type.totalRequests)) {

					statistic = new Statistic(Type.totalRequests.name());
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS));

					while (rs.next()) {
						statistic.getSeries().get(0).getNames().add(rs.getString("request_date"));
						statistic.getSeries().get(0).getValues().add(rs.getDouble("request_count"));
					}

				} else if (type.equals(Type.requestResponseTimes)) {
					
					statistic = new Statistic(Type.requestResponseTimes.name());
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS));
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_AVERAGE_RESPONSE_TIME));

					while (rs.next()) {

						if (statistic.getApi() == null) {
							statistic.setApi(rs.getString("api"));
							statistic.setApiVersion(rs.getString("api_version"));
						} else if (!(statistic.getApi().equals(rs.getString("api"))
								&& statistic.getApiVersion().equals(rs.getString("api_version")))) {
							statisticList.add(statistic);

							statistic = new Statistic(Type.requestResponseTimes.name());
							statistic.getSeries()
									.add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS));
							statistic.getSeries()
									.add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_AVERAGE_RESPONSE_TIME));

							statistic.setApi(rs.getString("api"));
							statistic.setApiVersion(rs.getString("api_version"));
						}

						statistic.getSeries().get(0).getNames()
								.add(rs.getString("method") + " " + rs.getString("resource_path"));
						statistic.getSeries().get(0).getValues().add(rs.getDouble("request_count"));
						statistic.getSeries().get(1).getNames()
								.add(rs.getString("method") + " " + rs.getString("resource_path"));
						statistic.getSeries().get(1).getValues().add(rs.getDouble("avg_response_time"));

					}
				}

				if (statistic != null) {
					statisticList.add(statistic);
				}
			}

		}
		// Handle any errors that may have occurred.
		catch (Exception exception) {
			logger.error("Error: ", exception);
			exception.printStackTrace();
			throw new InternalServerErrorException(ResponseUtils.serverError(exception));
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}

		return statisticList;

	}

	private List<Statistic> getApplicationStatistics(List<Type> types, String userName, String applicationName) {
		
		ArrayList<Statistic> statisticList = new ArrayList<Statistic>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();

			for (Type type : types) {

				Statistic statistic = null;

				stmt = con.prepareStatement(StatisticsUtil.SQL_APPLICATION_STATEMENT_MAP.get(type));
				stmt.setString(1, userName);
				stmt.setString(2, applicationName);

				// Handle new type of application name
				if (type.equals(Type.uniqueUsers)) {
					stmt.setString(3, userName.replace("@", "-AT-") + "_" + applicationName + "_PRODUCTION");
				}
				
				rs = stmt.executeQuery();

				if (type.equals(Type.uniqueUsers)) {

					statistic = new Statistic(Type.uniqueUsers.name());
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_UNIQUE_USERS));

					while (rs.next()) {
						statistic.getSeries().get(0).getNames().add(rs.getString("stat_date"));
						statistic.getSeries().get(0).getValues().add(rs.getDouble("unique_users"));
					}

				} else if (type.equals(Type.totalRequestsFaults)) {

					statistic = new Statistic(Type.totalRequestsFaults.name());
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS));
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_FAULTS));

					while (rs.next()) {
						statistic.getSeries().get(0).getNames().add(rs.getString("request_date"));
						statistic.getSeries().get(0).getValues().add(rs.getDouble("request_count"));
						statistic.getSeries().get(1).getNames().add(rs.getString("request_date"));
						statistic.getSeries().get(1).getValues().add(rs.getDouble("fault_count"));
					}
				} else if (type.equals(Type.faultsPercentage)) {

					statistic = new Statistic(Type.faultsPercentage.name());
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_FAULTS_PERCENTAGE));

					while (rs.next()) {
						statistic.getSeries().get(0).getNames().add(rs.getString("error_message"));
						statistic.getSeries().get(0).getValues().add(rs.getDouble("percentage"));
					}
				} else if (type.equals(Type.requestResponseTimes)) {

					statistic = new Statistic(Type.requestResponseTimes.name());
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS));
					statistic.getSeries().add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_AVERAGE_RESPONSE_TIME));

					while (rs.next()) {

						if (statistic.getApi() == null) {
							statistic.setApi(rs.getString("api"));
							statistic.setApiVersion(rs.getString("api_version"));
						} else if (!(statistic.getApi().equals(rs.getString("api"))
								&& statistic.getApiVersion().equals(rs.getString("api_version")))) {
							statisticList.add(statistic);

							statistic = new Statistic(Type.requestResponseTimes.name());
							statistic.getSeries()
									.add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS));
							statistic.getSeries()
									.add(new Series(StatisticsUtil.STATISTIC_SERIES_NAME_AVERAGE_RESPONSE_TIME));

							statistic.setApi(rs.getString("api"));
							statistic.setApiVersion(rs.getString("api_version"));
						}

						statistic.getSeries().get(0).getNames()
								.add(rs.getString("method") + " " + rs.getString("resource_path"));
						statistic.getSeries().get(0).getValues().add(rs.getDouble("request_count"));
						statistic.getSeries().get(1).getNames()
								.add(rs.getString("method") + " " + rs.getString("resource_path"));
						statistic.getSeries().get(1).getValues().add(rs.getDouble("avg_response_time"));

					}
				}

				if (statistic != null) {
					statisticList.add(statistic);
				}

			}
		} catch (SQLException e) {
			logger.error("SQLException: ", e);
			e.printStackTrace();
			throw new InternalServerErrorException(ResponseUtils.serverError(e));
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}

		return statisticList;

	}

}