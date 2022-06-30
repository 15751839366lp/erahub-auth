<template>
    <div id="departments">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>系统管理</el-breadcrumb-item>
            <el-breadcrumb-item>部门管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 右侧卡片区域 -->
        <!-- 用户列表卡片区 -->
        <el-card class="box-card">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-input
                            size="small"
                            clearable
                            v-model="queryMap.name"
                            placeholder="请输入部门查询"
                            @clear="search"
                            class="input-with-select"
                    >
                        <template #append>
                            <el-button icon="el-icon-search" @click="search"></el-button>
                        </template>

                    </el-input>
                </el-col>
                <el-col :span="2">
                    <el-button
                            size="small"
                            v-hasPermission="'department:add'"
                            type="success"
                            icon="el-icon-circle-plus-outline"
                            @click="addDialogVisible=true"
                    >添加
                    </el-button>
                </el-col>
                <el-col :span="2">
                    <el-button
                            size="small"
                            icon="el-icon-download"
                            v-hasPermission="'department:export'"
                            @click="downExcel"
                    >导出
                    </el-button>
                </el-col>
            </el-row>
            <!-- 表格区域 -->
            <el-table
                    border
                    size="small"
                    v-loading="loading"
                    stripe
                    :data="departmentData"
                    style="width: 100%;margin-top:20px;"
                    height="450"
            >
                <el-table-column prop="id" type="index" label="ID" width="50" fixed></el-table-column>
                <el-table-column prop="name" label="部门名" width="120" fixed></el-table-column>
                <el-table-column prop="phone" label="办公电话" width="180"></el-table-column>
                <el-table-column prop="total" label="人数" sortable width="100">
                    <template #default="scope">
                        <el-tag effect="plain" v-text="scope.row.total+'人'" size="small">
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" sortable></el-table-column>
                <el-table-column prop="modifiedTime" label="修改时间" sortable></el-table-column>
                <el-table-column prop="address" label="地址"></el-table-column>
                <el-table-column label="操作" width="200" fixed="right">
                    <template #default="scope">
                        <el-button
                                v-hasPermission="'department:edit'"
                                type="text"
                                size="small"
                                icon="el-icon-edit"
                                @click="editDepartment(scope.row.id)"
                        >编辑
                        </el-button>

                        <el-button
                                v-hasPermission="'department:delete'"
                                type="text"
                                size="small"
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
                    :current-page="this.queryMap.pageNum"
                    :page-sizes="[5, 10, 30, 50]"
                    :page-size="this.queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 部门别添加弹出框 -->
            <el-dialog title="添加部门" v-model="addDialogVisible" width="50%" @close="closeAddDialog">
        <span>
          <el-form
                  :model="addRuleForm"
                  :rules="addRules"
                  ref="addRuleFormRef"
                  label-width="100px"
                  class="demo-ruleForm"
          >
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="addRuleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="办公电话" prop="phone">
              <el-input v-model="addRuleForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="办公地址" prop="address">
              <el-input type="textarea" v-model="addRuleForm.address"></el-input>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addDepartment" :disabled="btnDisabled" :loading="btnLoading">确 定</el-button>
        </span>
                </template>
            </el-dialog>

            <!-- 部门别编辑弹出框 -->
            <el-dialog
                    title="更新部门"
                    v-model="editDialogVisible"
                    width="50%"
                    @close="closeEditDialog"
            >
        <span>
          <el-form
                  :model="editRuleForm"
                  :rules="addRules"
                  ref="editRuleFormRef"
                  label-width="100px"
                  class="demo-ruleForm"
          >
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="editRuleForm.name"></el-input>
            </el-form-item>
            <el-form-item label="办公电话" prop="phone">
              <el-input v-model="editRuleForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="办公地址" prop="address">
              <el-input type="textarea" v-model="editRuleForm.address"></el-input>
            </el-form-item>
          </el-form>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button
                  type="primary"
                  @click="updateDepartment"
                  :disabled="btnDisabled"
                  :loading="btnLoading"
          >确 定</el-button>
        </span>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {update, edit, findDepartmentList, deleteDepartment, add, excel} from '../../api/system/department'

    export default {
        setup() {
            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const editDialogVisible = ref(false)
            const addDialogVisible = ref(false) //添加弹框是否显示
            const total = ref(0) //总共多少条数据
            const departmentData = ref([]) //表格数据
            const queryMap = reactive({pageNum: 1, pageSize: 10, name: ""}) //查询对象
            const addRuleForm = ref({}) //添加表单数据
            const editRuleForm = ref({}) //修改表单数据
            const addRules = ref({
                name: [
                    {required: true, message: "请输入部门名称", trigger: "blur"},
                    {min: 3, max: 10, message: "长度在 3 到 10 个字符", trigger: "blur"}
                ],
                address: [
                    {required: true, message: "请输入办公地址", trigger: "blur"},
                    {min: 4, max: 12, message: "长度在 4 到 12 个字符", trigger: "blur"}
                ],
                phone: [
                    {
                        required: true,
                        message: "请输入联部门方式",
                        validator: (rule, value, callback) => {
                            const phoneReg = /^1[34578]\d{9}$$/;
                            if (!value) {
                                return callback(new Error("电话号码不能为空"));
                            }
                            setTimeout(() => {
                                if (!Number.isInteger(+value)) {
                                    callback(new Error("请输入数字值"));
                                } else {
                                    if (phoneReg.test(value)) {
                                        callback();
                                    } else {
                                        callback(new Error("电话号码格式不正确"));
                                    }
                                }
                            }, 100);
                        },
                        trigger: "blur"
                    }
                ]
            }) //添加验证

            const editRuleFormRef = ref(null)
            const addRuleFormRef = ref(null)

            /**
             * 加载部门表格
             */
            const downExcel = () => {
                excel().then(res => {
                    if (res.headers["content-type"] === "application/json") {
                        return ElMessage.error(
                            "Subject does not have permission [department:export]"
                        );
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    let a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "部门列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                });
            }
            /**
             * 搜索部门
             */
            const search = () => {
                queryMap.pageNum = 1;
                getDepartmentList();
            }
            /**
             * 删除部门
             */
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该部门, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if ("confirm" === res) {
                        deleteDepartment(
                            "system/department/delete/" + id
                        ).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("部门删除成功");
                                getDepartmentList();
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
            /**
             * 更新用户
             */
            const updateDepartment = () => {
                editRuleFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        (btnLoading.value = true), (btnDisabled.value = true);
                        update(
                            "system/department/update/" + editRuleForm.value.id,
                            editRuleForm.value
                        ).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "部门信息更新",
                                    type: "success"
                                });
                                getDepartmentList();
                            } else {
                                ElMessage.error("部门信息更新失败:" + res.data.data.errorMsg);
                            }
                            editRuleForm.value = {};
                            btnDisabled.value = false;
                            btnLoading.value = false;
                            editDialogVisible.value = false;
                        }).catch((res) => {
                            editRuleForm.value = {};
                            btnDisabled.value = false;
                            btnLoading.value = false;
                            editDialogVisible.value = false;
                            ElMessage.error("部门信息更新失败:" + res);
                        });

                    }
                });
            }
            /**
             * 编辑
             * @param {Object} id
             */
            const editDepartment = (id) => {
                edit("system/department/edit/" + id).then((res) => {
                    if (res.data.success) {
                        editRuleForm.value = res.data.data;
                    } else {
                        return ElMessage.error("部门信息编辑失败" + res.data.data.errorMsg);
                    }
                    editDialogVisible.value = true;
                }).catch((res) => {
                    editDialogVisible.value = true;
                    ElMessage.error("部门信息编辑失败" + res.data.data.errorMsg);
                });
            }
            //添加
            const addDepartment = () => {
                addRuleFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        (btnLoading.value = true), (btnDisabled.value = true);
                        add(addRuleForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("部门添加成功");
                                addRuleForm.value = {};
                                getDepartmentList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                addDialogVisible.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                ElMessage.error("部门添加失败:" + res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            addDialogVisible.value = false;
                            (btnLoading.value = false), (btnDisabled.value = false);
                            ElMessage.error("部门添加失败:" + res);
                        });
                    }
                });
            }
            //加载部门别列表
            const getDepartmentList = () => {
                loading.value = true;
                findDepartmentList(queryMap).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("获取用户列表失败:" + res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        departmentData.value = res.data.data.rows;
                    }
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("获取用户列表失败:" + res);
                });

            }
            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.pageSize = newSize;
                getDepartmentList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getDepartmentList();
            }
            //关闭弹出框
            const closeAddDialog = () => {
                addRuleFormRef.value.clearValidate();
                addRuleForm.value = {};
            }
            //关闭弹出框
            const closeEditDialog = () => {
                editRuleFormRef.value.clearValidate();
                editRuleForm.value = {};
            }

            getDepartmentList();

            return {
                btnLoading,
                btnDisabled,
                loading,
                editDialogVisible,
                addDialogVisible,
                total,
                departmentData,
                queryMap,
                addRuleForm,
                editRuleForm,
                addRules,
                editRuleFormRef,
                addRuleFormRef,
                downExcel,
                search,
                del,
                updateDepartment,
                editDepartment,
                addDepartment,
                getDepartmentList,
                handleSizeChange,
                handleCurrentChange,
                closeAddDialog,
                closeEditDialog
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