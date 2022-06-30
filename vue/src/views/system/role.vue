<template>
    <div id="roles">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>系统管理</el-breadcrumb-item>
            <el-breadcrumb-item>角色管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 卡片主体 -->
        <el-card class="box-card">
            <!-- 上面工具栏 -->
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-input
                            clearable
                            size="small"
                            placeholder="请输入角色名查询"
                            v-model="queryMap.roleName"
                            class="input-with-select"
                            @clear="getRoleList"
                    >
                        <template #append>
                            <el-button icon="el-icon-search" @click="search"></el-button>
                        </template>

                    </el-input>
                </el-col>
                <el-col :span="2">
                    <el-button
                            size="small"
                            v-hasPermission="'role:add'"
                            type="success"
                            icon="el-icon-circle-plus-outline"
                            @click="addDialogVisible=true"
                    >添加
                    </el-button>
                </el-col>
                <el-col :span="2">
                    <el-button
                            size="small"
                            v-hasPermission="'role:export'"
                            icon="el-icon-download"
                            @click="downExcel"
                    >导出
                    </el-button>
                </el-col>
            </el-row>
            <!-- 表格区域 -->
            <el-table
                    v-loading="loading"
                    :data="roleData"
                    border
                    style="width: 100%;margin-top:20px;"
                    height="450"
                    size="small"
            >
                <el-table-column prop="id" label="ID" width="180" fixed></el-table-column>
                <el-table-column prop="roleName" label="角色名" width="180" fixed></el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
                <el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column prop="isban" label="是否禁用" width="100">
                    <template #default="scope">
                        <el-switch v-model="scope.row.status" @change="changRoleStatus(scope.row)"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="200">
                    <template #default="scope">
                        <el-button

                                @click="grant(scope.row.id)"
                                type="text"
                                icon="el-icon-present"
                                size="small"
                        >授权
                        </el-button>
                        <el-button @click="edit(scope.row.id)" v-hasPermission="'role:edit'" type="text"
                                   icon="el-icon-edit" size="small">编辑
                        </el-button>
                        <el-button
                                v-hasPermission="'role:delete'"
                                @click="del(scope.row.id)"
                                type="text"
                                icon="el-icon-delete"
                                size="small"
                        >删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页部分 -->
            <el-pagination
                    background
                    style="margin-top:10px;"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[5, 10, 30, 50]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 添加弹框 -->
            <el-dialog title="添加角色" v-model="addDialogVisible" width="50%" @close="closeAdd">
                <el-form ref="addFormRef" :model="addForm" label-width="80px" :rules="addFormRoles">
                    <el-form-item label="角色名称" prop="roleName">
                        <el-input v-model="addForm.roleName"></el-input>
                    </el-form-item>
                    <el-form-item label="描述信息" prop="remark">
                        <el-input type="textarea" v-model="addForm.remark"></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button
                  type="primary"
                  @click="addRole"
                  :loading="btnLoading"
                  :disabled="btnDisabled"
          >确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 编辑弹框 -->
            <el-dialog title="编辑角色" v-model="editDialogVisible" width="50%" @close="closeEdit">
                <el-form ref="editFormRef" :model="editForm" label-width="80px" :rules="addFormRoles">
                    <el-form-item label="角色名称" prop="roleName">
                        <el-input v-model="editForm.roleName"></el-input>
                    </el-form-item>
                    <el-form-item label="描述信息" prop="remark">
                        <el-input type="textarea" v-model="editForm.remark"></el-input>
                    </el-form-item>
                </el-form>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button
                  type="primary"
                  @click="updateRole"
                  :loading="btnLoading"
                  :disabled="btnDisabled"
          >确 定</el-button>
        </span>
                </template>
            </el-dialog>
            <!-- 角色授权弹出框 -->
            <el-dialog title="分配菜单权限" v-model="grantDialogVisible" width="38%">
        <span>
          <el-tree
                  :auto-expand-parent="false"
                  :data="data"
                  show-checkbox
                  node-key="id"
                  :default-expanded-keys="open"
                  :props="defaultProps"
                  ref="authorityTree"
                  highlight-current
          ></el-tree>
            <!-- check-strictly -->
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="grantDialogVisible = false">取 消</el-button>
          <el-button
                  v-hasPermission="'role:authority'"
                  type="primary"
                  icon="el-icon-setting"
                  @click="authorityRole"
                  :loading="btnLoading"
                  :disabled="btnDisabled"
          >授 权</el-button>
        </span>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>

<script>
    import {ref, reactive} from "vue";
    import {ElMessage, ElNotification, ElMessageBox} from "element-plus";
    import {
        findRoleMenu,
        findRoleList,
        add,
        editRole,
        update,
        deleteRole,
        updateStatus,
        authority,
        excel
    } from '../../api/system/role'

    export default {
        setup() {


            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const total = ref(0)
            const queryMap = reactive({roleName: "", pageNum: 1, pageSize: 10}) //查询条件
            const roleData = ref([]) //角色表格数据
            const addForm = ref({}) //添加数据
            const editForm = ref({}) //编辑数据
            const addDialogVisible = ref(false) //添加弹框的显示
            const editDialogVisible = ref(false) //编辑弹框
            const grantDialogVisible = ref(false) //授权弹出框
            const data = ref([])
            const open = ref([]) //展开
            const grantId = ref("")
            const defaultProps = ref({
                children: "children",
                label: "menuName"
            })

            const authorityTree = ref({})
            const addFormRef = ref({})
            const editFormRef = ref({})

            const addFormRoles = reactive({
                roleName: [
                    {required: true, message: "请输入角色名称", trigger: "blur"},
                    {min: 3, max: 10, message: "长度在 3 到 10 个字符", trigger: "blur"}
                ],
                remark: [
                    {required: true, message: "请输入角色描述信息", trigger: "blur"},
                    {min: 5, max: 20, message: "长度在 5 到 20 个字符", trigger: "blur"}
                ]
            }) //添加验证规则

            /**
             * 加载菜单表格
             */
            const downExcel = () => {
                excel().then(res => {
                    if (res.headers['content-type'] === 'application/json') {
                        return ElMessage.error("Subject does not have permission [role:export]");
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "角色列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                });
            }

            //获取选中的节点
            const authorityRole = () => {
                btnDisabled.value = true;
                btnLoading.value = true;
                authority(
                    "system/role/authority/" + grantId.value,
                    [].concat(
                        authorityTree.value.getCheckedKeys(),
                        authorityTree.value.getHalfCheckedKeys()
                    )
                ).then((res) => {
                    if (res.data.success) {
                        ElMessage.success("角色授权成功");
                    } else {
                        ElMessage.error("角色授权失败:" + res.data.data.errorMsg);
                    }
                    btnDisabled.value = false;
                    btnLoading.value = false;
                    grantDialogVisible.value = false;
                }).catch((res) => {
                    ElMessage.error("角色授权失败:" + res);
                    btnDisabled.value = false;
                    btnLoading.value = false;
                    grantDialogVisible.value = false;
                });
            }

            //用户授权
            const grant = (id) => {
                //加载所有菜单以及用户拥有的菜单权限id
                findRoleMenu("system/role/findRoleMenu/" + id).then((res) => {
                    if (res.data.success) {
                        //默认选中的树的数据
                        setTimeout(function () {
                            res.data.data.mids.forEach(val => {
                                authorityTree.value.setChecked(val, true, false);
                            });
                        }, 100);
                        data.value = res.data.data.tree;
                        open.value = res.data.data.open;
                        grantId.value = id; //需要授权的id
                    }
                    grantDialogVisible.value = true;
                }).catch((res) => {
                    ElMessage.error("角色授权失败:" + res);
                    grantDialogVisible.value = true;
                });
            }
            //加载用户列表
            const getRoleList = () => {
                loading.value = true;
                findRoleList(queryMap).then((res) => {
                    if (res.data.success) {
                        roleData.value = res.data.data.rows;
                        total.value = res.data.data.total;
                        loading.value = false;
                    }else {
                        loading.value = false;
                        ElMessage.error("加载用户列表权失败:" + res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("加载用户列表权失败:" + res);
                });

            }

            //搜索
            const search = () => {
                queryMap.pageNum = 1;
                getRoleList();
            }
            //关闭添加弹框
            const closeAdd = () => {
                addFormRef.value.clearValidate();
                addForm.value = {};
            }
            const closeEdit = () => {
                editFormRef.value.clearValidate();
                editForm.value = {};
            }
            //添加
            const addRole = () => {
                addFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnDisabled.value = true;
                        btnLoading.value = true;
                        add(addForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("添加成功");
                                addDialogVisible.value = false;
                                btnDisabled.value = false;
                                btnLoading.value = false;
                                addForm.value = {};
                                getRoleList();
                            } else {
                                btnDisabled.value = false;
                                btnLoading.value = false;
                                return ElMessage.error("角色添加失败:" + res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error("角色添加失败:" + res);
                        });
                    }
                });
            }
            //编辑
            const edit = (id) => {
                editRole("system/role/edit/" + id).then((res) => {
                    if (res.data.success) {
                        editForm.value = res.data.data;
                        editDialogVisible.value = true;
                    } else {
                        return ElMessage.error("角色编辑失败:" + res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    ElMessage.error("角色编辑失败:" + res);
                });
            }
            //更新用户
            const updateRole = () => {
                editFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnDisabled.value = true;
                        btnLoading.value = true;
                        update(
                            "system/role/update/" + editForm.value.id,
                            editForm.value
                        ).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    title: "成功",
                                    message: "角色信息更新",
                                    type: "success"
                                });
                                getRoleList();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                ElMessage.error("角色信息更新失败:" + res.data.data.errorMsg);
                            }

                            editDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                            editForm.value = {};
                        }).catch((res) => {
                            ElMessage.error("角色信息更新失败:" + res);
                            editDialogVisible.value = false;
                            btnDisabled.value = false;
                            btnLoading.value = false;
                            editForm.value = {};
                        });
                    }
                });
            }
            //删除
            const del = (id) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该角色, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        deleteRole("system/role/delete/" + id).then((res) => {
                            console.log(res);
                            if (res.data.success) {
                                ElMessage.success("删除成功");
                                getRoleList();
                            } else {
                                ElMessage.error("删除失败:" + res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error("删除失败:" + res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });

            }
            //改变用户禁用状态
            const changRoleStatus = (row) => {
                updateStatus(
                    "system/role/updateStatus/" + row.id + "/" + row.status
                ).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("更新状态失败:" + res.data.data.errorMsg);
                        row.status = !row.status;
                    } else {
                        ElMessage.success("更新状态成功");
                    }
                }).catch((res) => {
                    ElMessage.error("更新状态失败:" + res);
                });

            }
            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.pageSize = newSize;
                getRoleList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.pageNum = current;
                getRoleList();
            }


            getRoleList();


            return {
                btnLoading,
                btnDisabled,
                loading,
                total,
                queryMap, //查询条件
                roleData, //角色表格数据
                addForm, //添加数据
                editForm, //编辑数据
                addDialogVisible, //添加弹框的显示
                editDialogVisible, //编辑弹框
                grantDialogVisible, //授权弹出框
                data,
                open, //展开
                grantId,
                defaultProps,
                addFormRoles,
                authorityTree,
                addFormRef,
                editFormRef,
                downExcel,
                authorityRole,
                grant,
                getRoleList,
                search,
                closeAdd,
                closeEdit,
                addRole,
                edit,
                updateRole,
                del,
                changRoleStatus,
                handleSizeChange,
                handleCurrentChange,
            };
        },
    };
</script>
<style>
    .el-notification__icon.el-icon-success{
        color: #67c23a;
    }
    .el-notification__icon.el-icon-warning{
        color: #e6a23c;
    }
    .el-notification__icon.el-icon-info{
        color: #909399;
    }
    .el-notification__icon.el-icon-error{
        color: #f56c6c;
    }
</style>
