package org.vasttrafik.wso2.carbon.apimgt.portal.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;

import org.vasttrafik.wso2.carbon.common.api.utils.ResponseUtils;

public class StatisticsUtil implements ResourceBundleAware {

	public static final Map<String, String> SQL_API_STATEMENT_MAP;
	public static final Map<Type, String> SQL_APPLICATION_STATEMENT_MAP;
	public static final List<String> SQL_DELETE_STATEMENT_LIST;

	public static final String STATISTIC_SERIES_NAME_NUMBER_OF_REQUESTS = "Antal requests";
	public static final String STATISTIC_SERIES_NAME_NUMBER_OF_FAULTS = "Antal fel";
	public static final String STATISTIC_SERIES_NAME_AVERAGE_RESPONSE_TIME = "Genomsnittlig svarstid";
	public static final String STATISTIC_SERIES_NAME_UNIQUE_USERS = "Unika användare";
	public static final String STATISTIC_SERIES_NAME_FAULTS_PERCENTAGE = "Procentuella fel";

	public static enum Type {
		uniqueUsers, totalRequests, totalRequestsFaults, faultsPercentage, requestResponseTimes;

		public static Type fromString(String param) {
			try {
				return valueOf(param);
			} catch (Exception e) {
				throw new BadRequestException(
						ResponseUtils.badRequest(resourceBundle, 1000L, new Object[][] { { param }, { "Type" } }));
			}
		}
	}

	public static enum Source {
		apis, applications;

		public static Source fromString(String param) {
			try {
				return valueOf(param);
			} catch (Exception e) {
				throw new BadRequestException(
						ResponseUtils.badRequest(resourceBundle, 1000L, new Object[][] { { param }, { "Source" } }));
			}
		}
	}

	public static enum Period {
		week, month, quarter;

		public static Period fromString(String param) {
			try {
				return valueOf(param);
			} catch (Exception e) {
				throw new BadRequestException(
						ResponseUtils.badRequest(resourceBundle, 1000L, new Object[][] { { param }, { "Period" } }));
			}
		}
	}

	public static enum Grouping {
		day, week, month;

		public static Grouping fromString(String param) {
			try {
				return valueOf(param);
			} catch (Exception e) {
				throw new BadRequestException(
						ResponseUtils.badRequest(resourceBundle, 1000L, new Object[][] { { param }, { "Grouping" } }));
			}
		}
	}

	static {

		/* Map api, type and period to SQL statement */
		SQL_API_STATEMENT_MAP = new HashMap<String, String>();
		SQL_API_STATEMENT_MAP.put(Type.totalRequests.name() + Period.week,
				"SELECT CONVERT(VARCHAR(10), request_date, 20) as request_date, sum(request_count) as request_count FROM dbo.API_TRAFFIC_SUMMARY_PUBLIC_STAT_VIEW WHERE request_date > DATEADD(DAY, -9, GETDATE()) AND api LIKE ? and api_version LIKE ? AND resource_path IS NOT NULL AND method = 'GET' GROUP BY CONVERT(VARCHAR(10), request_date, 20) ORDER BY request_date");
		SQL_API_STATEMENT_MAP.put(Type.totalRequests.name() + Period.month,
				"SELECT DATEPART (wk, request_date) as request_date, DATEPART (year, request_date) as year, sum(request_count) as request_count FROM dbo.API_TRAFFIC_SUMMARY_PUBLIC_STAT_VIEW WHERE request_date > DATEADD(MONTH, -1, GETDATE()) AND api LIKE ? and api_version LIKE ? AND resource_path IS NOT NULL AND method = 'GET' GROUP BY DATEPART (wk, request_date), DATEPART (year, request_date) ORDER BY DATEPART (year, request_date), DATEPART (wk, request_date)");
		SQL_API_STATEMENT_MAP.put(Type.totalRequests.name() + Period.quarter,
				"SELECT DATEPART (wk, request_date) as request_date, DATEPART (year, request_date) as year, sum(request_count) as request_count FROM dbo.API_TRAFFIC_SUMMARY_PUBLIC_STAT_VIEW WHERE request_date > DATEADD(MONTH, -3, GETDATE()) AND api LIKE ? and api_version LIKE ? AND resource_path IS NOT NULL AND method = 'GET' GROUP BY DATEPART (wk, request_date), DATEPART (year, request_date) ORDER BY DATEPART (year, request_date), DATEPART (wk, request_date)");
		SQL_API_STATEMENT_MAP.put(Type.requestResponseTimes.name() + Period.week,
				"SELECT resource_path, method, api, api_version, sum(request_count) as request_count, SUM(CAST(request_count * avg_response_time AS BIGINT)) / SUM(request_count) as avg_response_time FROM dbo.API_TRAFFIC_SUMMARY_PUBLIC_STAT_VIEW WHERE request_date > DATEADD(DAY, -9, GETDATE()) AND api LIKE ? AND api_version LIKE ? AND resource_path IS NOT NULL AND method = 'GET' GROUP BY resource_path, method, api, api_version ORDER BY api, api_version");
		SQL_API_STATEMENT_MAP.put(Type.requestResponseTimes.name() + Period.month,
				"SELECT resource_path, method, api, api_version, sum(request_count) as request_count, SUM(CAST(request_count * avg_response_time AS BIGINT)) / SUM(request_count) as avg_response_time FROM dbo.API_TRAFFIC_SUMMARY_PUBLIC_STAT_VIEW WHERE request_date > DATEADD(MONTH, -1, GETDATE()) AND api LIKE ? AND api_version LIKE ? AND resource_path IS NOT NULL AND method = 'GET' GROUP BY resource_path, method, api, api_version ORDER BY api, api_version");
		SQL_API_STATEMENT_MAP.put(Type.requestResponseTimes.name() + Period.quarter,
				"SELECT resource_path, method, api, api_version, sum(request_count) as request_count, SUM(CAST(request_count * avg_response_time AS BIGINT)) / SUM(request_count) as avg_response_time FROM dbo.API_TRAFFIC_SUMMARY_PUBLIC_STAT_VIEW WHERE request_date > DATEADD(MONTH, -3, GETDATE()) AND api LIKE ? AND api_version LIKE ? AND resource_path IS NOT NULL AND method = 'GET' GROUP BY resource_path, method, api, api_version ORDER BY api, api_version");

		/* Map type to SQL statement */
		SQL_APPLICATION_STATEMENT_MAP = new HashMap<Type, String>();
		SQL_APPLICATION_STATEMENT_MAP.put(Type.totalRequestsFaults,
				"SELECT CONVERT(VARCHAR(10), request_date, 20) as request_date, sum(request_count) as request_count, sum(fault_count) as fault_count FROM dbo.API_TRAFFIC_SUMMARY_STAT_APPLICATION WHERE request_date > DATEADD(DAY, -9, GETDATE()) AND user_id LIKE ? AND application_name LIKE ? GROUP BY CONVERT(VARCHAR(10), request_date, 20) ORDER BY request_date");
		SQL_APPLICATION_STATEMENT_MAP.put(Type.uniqueUsers,
				"SELECT stat_date, sum(unique_users) as unique_users FROM dbo.API_APP_USERS_VIEW WHERE stat_date > DATEADD(DAY, -8, GETDATE()) AND username LIKE ? AND (app_name LIKE ? OR app_name LIKE ?) AND app_name != 'DefaultApplication' GROUP BY stat_date ORDER BY stat_date");
		SQL_APPLICATION_STATEMENT_MAP.put(Type.faultsPercentage,
				"SELECT error_message, sum(fault_count) as fault_count, cast(100.0 * SUM(fault_count) / SUM(sum(fault_count)) over () as decimal(6,1)) as percentage FROM dbo.API_ERRORS_STAT_APPLICATION_VIEW WHERE request_datetime > DATEADD(DAY, -9, GETDATE()) AND user_id LIKE ? AND application_name LIKE ? GROUP BY error_message");
		SQL_APPLICATION_STATEMENT_MAP.put(Type.requestResponseTimes,
				"SELECT resource_path, method, api, api_version, sum(request_count) as request_count, SUM(CAST(request_count * avg_response_time AS BIGINT)) / SUM(request_count) as avg_response_time FROM dbo.API_TRAFFIC_SUMMARY_STAT_APPLICATION WHERE request_date > DATEADD(DAY, -9, GETDATE()) AND resource_path IS NOT NULL AND user_id LIKE ? AND application_name LIKE ? GROUP BY resource_path, method, api, api_version ORDER BY api, api_version");

	
		/* List of statements to anonymize statistics usage for a user */
		SQL_DELETE_STATEMENT_LIST = new ArrayList<String>();
		SQL_DELETE_STATEMENT_LIST.add("UPDATE dbo.API_FAULT_SUMMARY SET user_id = ? WHERE user_id = ?");
		SQL_DELETE_STATEMENT_LIST.add("UPDATE dbo.API_TRAFFIC_SUMMARY SET user_id = ? WHERE user_id = ?");
		SQL_DELETE_STATEMENT_LIST.add("UPDATE dbo.API_OAUTH2_ACCESS_TOKEN_HISTORY_CURRENT SET authz_user = ? WHERE authz_user = ?");
	}

}
