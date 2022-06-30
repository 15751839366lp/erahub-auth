<template>
    <div id="LoginLogs">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>日志管理</el-breadcrumb-item>
            <el-breadcrumb-item>登入日志</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 右侧卡片区域 -->
        <!-- 用户列表卡片区 -->
        <el-card class="box-card">
            <el-form size="mini" :inline="true" :model="queryMap" class="demo-form-inline">
                <el-form-item label="用户名">
                    <el-input v-model="queryMap.username" placeholder="请输入用户名查询"></el-input>
                </el-form-item>
                <el-form-item label="IP地址">
                    <el-input v-model="queryMap.ip" placeholder="请输入IP查询"></el-input>
                </el-form-item>
                <el-form-item label="登入地址">
                    <el-input v-model="queryMap.location" placeholder="请输入登入地址查询"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button icon="el-icon-search" @click="search" type="primary">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button @click="deleteFileOrDirectory(selections)" icon="el-icon-delete"
                               :disabled="selections.length === 0">批量
                    </el-button>
                </el-form-item>
            </el-form>
            <!-- 表格区域 -->
            <el-table
                    border
                    stripe
                    size="mini"
                    :data="LoginLogData"
                    style="width: 100%;"
                    height="450"
                    @selection-change="selsChange"
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" type="index" label="ID" width="50"></el-table-column>
                <el-table-column prop="username" label="登入用户" width="150"></el-table-column>
                <el-table-column prop="loginTime" label="登入时间" width="190"></el-table-column>
                <el-table-column prop="location" label="登入地点" width="250"></el-table-column>
                <el-table-column prop="ip" label="IP地址"></el-table-column>
                <el-table-column prop="userSystem" label="操作系统" width="150"></el-table-column>
                <el-table-column prop="userBrowser" label="浏览器"></el-table-column>
                <el-table-column label="操作" width="100px;">
                    <template #default="scope">
                        <el-button
                                v-hasPermission="'loginLog:delete'"
                                type="text"
                                size="mini"
                                icon="el-icon-delete"
                                @click="del(scope.row.id)"
                        >删除
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
                    :page-sizes="[ 10, 30, 50]"
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
    import {findLoginLogList, deleteLog, batchDelete} from '../../api/system/loginLog'

    export default {

        setup() {
            const selections = ref([])   //选中的值显示
            const LoginLogData = ref([])
            const total = ref(0) //总共多少条数据
            const queryMap = reactive({pageNum: 1, pageSize: 10, location: ""}) //查询对象

            const deleteFileOrDirectory = () => {
                let ids = selections.value.map(item => item.id).join(); //获取所有选中行的id组成的字符串，以逗号分隔
                ElMessageBox.confirm(
                    "此操作将永久批量删除登入日志, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        batchDelete("system/loginLog/batchDelete/" + ids).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("登入日志删除成功");
                                getLoginLogList();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch((res) => {
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
                getLoginLogList();
            }

            //加载登入日志列表
            const getLoginLogList = () => {
                findLoginLogList(queryMap).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取列表失败:" + res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        LoginLogData.value = res.data.data.rows;
                    }
                }).catch((res) => {
                    ElMessage.error("获取列表失败:" + res);
                });
            }
            //删除登入日志
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该登入日志, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteLog("system/loginLog/delete/" + id).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("登入日志删除成功");
                                getLoginLogList();
                            } else {
                                ElMessage.error(res.data.data.errorMsg);
                            }
                        }).catch((res) => {
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
                getLoginLogList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getLoginLogList();
            }

            getLoginLogList();

            return {
                selections,
                LoginLogData,
                total,
                queryMap,
                deleteFileOrDirectory,
                selsChange,
                search,
                getLoginLogList,
                handleCurrentChange,
                handleSizeChange,
                del,
            }
        },
    };
</script>
<style>
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