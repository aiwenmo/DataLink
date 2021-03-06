package com.datalink.flink.executor;

import com.datalink.flink.executor.custom.CustomTableEnvironmentImpl;
import com.datalink.flink.result.SqlExplainResult;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.ExplainDetail;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.functions.UserDefinedFunction;

/**
 * LocalStreamExecuter
 *
 * @author qiwenkai
 * @since 2021/5/25 13:48
 **/
public class LocalStreamExecutor extends Executor {

    private StreamExecutionEnvironment environment;
    private CustomTableEnvironmentImpl stEnvironment;
    private ExecutorSetting executorSetting;

    public LocalStreamExecutor(ExecutorSetting executorSetting) {
        this.executorSetting = executorSetting;
        this.environment = StreamExecutionEnvironment.createLocalEnvironment();
        stEnvironment = CustomTableEnvironmentImpl.create(environment);
        if(executorSetting.isUseSqlFragment()){
            stEnvironment.useSqlFragment();
        }else{
            stEnvironment.unUseSqlFragment();
        }
    }

    @Override
    public StreamExecutionEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public CustomTableEnvironmentImpl getCustomTableEnvironmentImpl() {
        return this.stEnvironment;
    }

    @Override
    public ExecutorSetting getExecutorSetting() {
        return this.executorSetting;
    }

    @Override
    public EnvironmentSetting getEnvironmentSetting() {
        return null;
    }

    @Override
    public JobExecutionResult execute(String statement) throws Exception {
        return stEnvironment.execute(statement);
    }

    @Override
    public TableResult executeSql(String statement) {
        return stEnvironment.executeSql(statement);
    }

    @Override
    public Table sqlQuery(String statement) {
        return stEnvironment.sqlQuery(statement);
    }

    @Override
    public String explainSql(String statement, ExplainDetail... extraDetails) {
        return stEnvironment.explainSql(statement,extraDetails);
    }

    @Override
    public SqlExplainResult explainSqlRecord(String statement, ExplainDetail... extraDetails) {
        return stEnvironment.explainSqlRecord(statement,extraDetails);
    }

    @Override
    public String getStreamGraphString(String statement) {
        return stEnvironment.getStreamGraphString(statement);
    }

    @Override
    public ObjectNode getStreamGraph(String statement) {
        return stEnvironment.getStreamGraph(statement);
    }

    @Override
    public void registerFunction(String name, ScalarFunction function) {
        stEnvironment.registerFunction(name,function);
    }

    @Override
    public void createTemporarySystemFunction(String name, Class<? extends UserDefinedFunction> var2) {
        stEnvironment.createTemporarySystemFunction(name,var2);
    }

}
