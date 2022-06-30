<template>
    <div id="Logs">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>日志管理</el-breadcrumb-item>
            <el-breadcrumb-item>系统日志</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 右侧卡片区域 -->
        <!-- 用户列表卡片区 -->
        <el-card class="box-card">

            <el-form :inline="true" size="mini" :model="queryMap" class="demo-form-inline">
                <el-form-item label="操作人">
                    <el-input @keyup.enter.native="search" clearable @clear="search" v-model="queryMap.username"
                              placeholder="操作人"></el-input>
                </el-form-item>
                <el-form-item label="ip地址">
                    <el-input @keyup.enter.native="search" clearable @clear="search" v-model="queryMap.ip"
                              placeholder="ip地址"></el-input>
                </el-form-item>

                <el-form-item label="操作位置">
                    <el-input @keyup.enter.native="search" clearable @clear="search" v-model="queryMap.location"
                              placeholder="操作位置"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search" icon="el-icon-search">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button @click="deleteFileOrDirectory(selections)" :disabled="selections.length === 0"
                               class="el-icon-delete">批量
                    </el-button>
                </el-form-item>
            </el-form>

            <!-- 表格区域 -->
            <el-table
                    size="mini"
                    border
                    stripe
                    :data="LogData"
                    style="width: 100%;"
                    height="450"
                    @selection-change="selsChange"
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="operation" label="操作" width="150"></el-table-column>

                <el-table-column :show-overflow-tooltip="true" prop="method" label="方法" width="180">

                </el-table-column>

                <el-table-column :show-overflow-tooltip="true" prop="params" label="参数" width="100">
                    <template #default="scope">
                        <span>{{ scope.row.params }}</span>
                    </template>
                </el-table-column>

                <el-table-column prop="time" label="耗时" width="120" sortable>
                    <template #default="scope">
                        <el-tag v-if="scope.row.time>=2000" size="mini" type="danger">
                            {{scope.row.time+'毫秒'}}
                        </el-tag>
                        <el-tag size="mini" v-else-if="scope.row.time>=1000&&scope.row.time<=2000">
                            {{scope.row.time+'毫秒'}}
                        </el-tag>
                        <el-tag v-else type="success" size="mini">
                            {{scope.row.time+'毫秒'}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="location" label="操作地点" width="240"></el-table-column>
                <el-table-column prop="ip" label="IP地址" width="160"></el-table-column>
                <el-table-column prop="username" label="操作人" width="150"></el-table-column>
                <el-table-column prop="createTime" label="时间" sortable width="180"></el-table-column>


                <el-table-column label="操作" width="100px;" fixed="right">
                    <template #default="scope">
                        <el-button type="text" size="mini" v-hasPermission="'log:delete'" icon="el-icon-delete"
                                   @click="del(scope.row.id)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页 -->
            <el-pagination
                    style="margin-top:10px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[10, 30, 50]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {findLogList, deleteLog, batchDelete} from '../../api/system/log'

    export default {

        setup() {
            const selections = ref([]) //选中的值显示
            const LogData = ref([])
            const total = ref(0) //总共多少条数据
            const queryMap = reactive({pageNum: 1, pageSize: 10, location: ""}) //查询对象

            const deleteFileOrDirectory = () => {
                const ids = selections.value.map(item => item.id).join(); //获取所有选中行的id组成的字符串，以逗号分隔
                ElMessageBox.confirm(
                    "此操作将永久批量删除系统日志, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        batchDelete("system/log/batchDelete/" + ids).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("系统日志删除成功");
                                getLogList();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch(() => {
                            ElMessage.error(res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            }
            const selsChange = (sels) => {
                selections.value = sels;
            }
            //搜索
            const search = () => {
                queryMap.pageNum = 1;
                getLogList();
            }

            //加载系统日志列表
            const getLogList = () => {
                findLogList(queryMap).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取列表失败");
                    } else {
                        total.value = res.data.data.total;
                        LogData.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    return ElMessage.error("获取列表失败");
                });

            }
            //删除系统日志
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该系统日志, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteLog("system/log/delete/" + id).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("系统日志删除成功");
                                getLogList();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch(() => {
                            ElMessage.error(res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            }

            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.pageSize = newSize;
                getLogList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getLogList();
            }

            getLogList();

            return {
                selections,
                LogData,
                total,
                queryMap,
                deleteFileOrDirectory,
                selsChange,
                search,
                getLogList,
                del,
                handleSizeChange,
                handleCurrentChange,
            };
        },
    };
</script>

<style>
    .el-tooltip__popper {
        max-width: 400px;
    }

    .el-notification__icon.el-icon-success {
        color: #67c23a;
    }

    .el-notification__icon.el-icon-warning {
        color: #e6a23c;
    }

    .el-notification__icon.el-icon-info {
        color: #909399;
    }

    .el-notification__icon.el-icon-error {
        color: #f56c6c;
    }
</style>
