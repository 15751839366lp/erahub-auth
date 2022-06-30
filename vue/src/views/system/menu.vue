<template>
    <div id="roles" v-loading="loading">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>系统管理</el-breadcrumb-item>
            <el-breadcrumb-item>菜单管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 卡片主体 -->
        <el-card class="box-card">
            <div class="block">
                <!-- 节点过滤 -->
                <el-row>
                    <el-col :span="16">
                        <div class="grid-content bg-purple">
                            <el-input placeholder="输入关键字进行过滤" v-model="filterText" clearable></el-input>
                        </div>
                    </el-col>
                    <el-col :span="2">
                        <div class="grid-content bg-purple-light">
                            <el-button
                                    type="primary"
                                    icon="el-icon-plus"
                                    style="margin-left:20px;"
                                    @click="openParentAdd"
                            >父级
                            </el-button>
                        </div>
                    </el-col>
                    <el-col :span="2">
                        <el-button
                                v-hasPermission="'menu:export'"
                                style="margin-left:20px;"
                                icon="el-icon-download"
                                @click="downExcel"
                        >导出
                        </el-button>
                    </el-col>

                </el-row>

                <p>
                    菜单权限树
                </p>

                <el-tree
                        :data="data"
                        :auto-expand-parent="false"
                        node-key="id"
                        show-checkbox
                        :default-expanded-keys="open"
                        :expand-on-click-node="false"
                        :render-content="renderContent"
                        :props="defaultProps"
                        highlight-current
                        :filter-node-method="filterNode"
                        ref="treeRef"
                ></el-tree>
            </div>
        </el-card>
        <!-- 节点新增弹出框 -->
        <el-dialog :title="addTitle" v-model="addDialogVisible" @close="addClose" width="50%">
      <span>
        <el-form
                size="mini"
                ref="addFormRef"
                :model="addForm"
                label-width="80px"
                :rules="addFormRules"
        >
          <el-form-item label="节点名称" prop="menuName">
            <el-input v-model="addForm.menuName"></el-input>
          </el-form-item>
          <el-form-item label="URL">
            <el-input v-model="addForm.url"></el-input>
          </el-form-item>
          <el-form-item label="权限编码">
            <el-input v-model="addForm.perms"></el-input>
          </el-form-item>
          <el-form-item label="图标">
            <el-input v-model="addForm.icon"></el-input>
          </el-form-item>
          <el-form-item label="是否可用" prop="disabled">
              <el-radio v-model="addForm.disabled" label="false">可用</el-radio>
              <el-radio v-model="addForm.disabled" label="true">禁用</el-radio>
          </el-form-item>
          <el-form-item label="是否展开" prop="open">
              <el-radio v-model="addForm.open" label="1">展开</el-radio>
              <el-radio v-model="addForm.open" label="0">关闭</el-radio>
          </el-form-item>
          <el-form-item label="排序" prop="orderNum">
            <el-input-number v-model="addForm.orderNum" :min="1" :max="10" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="类型" prop="type">
              <el-radio v-model="addForm.type" label="0">菜单</el-radio>
              <el-radio v-model="addForm.type" label="1">按钮</el-radio>
          </el-form-item>
        </el-form>
      </span>
            <template #footer>
            <span class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addNode" :loading="btnLoading" :disabled="btnDisabled">确 定</el-button>
      </span>
            </template>
        </el-dialog>
        <!-- 编辑节点弹出框 -->
        <el-dialog :title="editTitle" v-model="editlogVisible" width="50%" @close="editClose">
      <span>
        <el-form
                size="mini"
                ref="editFormRef"
                :model="editForm"
                label-width="80px"
                :rules="addFormRules"
        >
          <el-form-item label="节点名称" prop="menuName">
            <el-input v-model="editForm.menuName"></el-input>
          </el-form-item>
          <el-form-item label="URL">
            <el-input v-model="editForm.url"></el-input>
          </el-form-item>
          <el-form-item label="权限编码">
            <el-input v-model="editForm.perms"></el-input>
          </el-form-item>
          <el-form-item label="图标">
            <el-input v-model="editForm.icon"></el-input>
          </el-form-item>
          <el-form-item label="是否可用" prop="disabled">
              <el-radio v-model="editForm.disabled" :label="false">可用</el-radio>
              <el-radio v-model="editForm.disabled" :label="true">禁用</el-radio>
          </el-form-item>
          <el-form-item label="是否展开" prop="open">
              <el-radio v-model="editForm.open" :label="1">展开</el-radio>
              <el-radio v-model="editForm.open" :label="0">关闭</el-radio>
          </el-form-item>
          <el-form-item label="排序" prop="orderNum">
            <el-input-number v-model="editForm.orderNum" :min="1" :max="10" label="描述文字"></el-input-number>
          </el-form-item>
          <el-form-item label="类型" prop="type">
              <el-radio v-model="editForm.type" :label="0">菜单</el-radio>
              <el-radio v-model="editForm.type" :label="1">按钮</el-radio>
          </el-form-item>
        </el-form>
      </span>
            <template #footer>
            <span class="dialog-footer">
        <el-button @click="editlogVisible = false">取 消</el-button>
        <el-button
                type="primary"
                @click="updateMenu"
                :loading="btnLoading"
                :disabled="btnDisabled"
        >确 定</el-button>
      </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    import {ref, reactive, computed, watch} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {update, editMenu, tree, deleteMenu, add, excel} from '../../api/system/menu'

    let id = 1000;

    export default {
        setup() {
            const data = ref([]);

            const btnLoading = ref(false)
            const btnDisabled = ref(false)
            const loading = ref(true)
            const open = ref([])  //展开节点
            const filterText = ref("") //节点过滤文本
            const addDialogVisible = ref(false) //新增节点弹出框
            const editlogVisible = ref(false) //编辑节点弹出框
            const addTitle = ref("")
            const editTitle = ref("")
            const addForm = ref({
                parentId: "",
                menuName: "",
                url: "",
                type: "",
                orderNum: "",
                disabled: "",
                open: "",
                perms: ""
            }) //添加请求表单数据
            const editForm = ref(null) //编辑节点表单数据
            const addFormRules = ref({
                menuName: [
                    {required: true, message: "节点名称不能为空", trigger: "blur"},
                    {min: 3, max: 10, message: "长度在 3 到 10 个字符", trigger: "blur"}
                ],
                disabled: [
                    {required: true, message: "节点状态不能为空", trigger: "blur"}
                ],

                orderNum: [
                    {required: true, message: "排序不能为空", trigger: "blur"}
                ],
                type: [{required: true, message: "类型不能为空", trigger: "blur"}],
                open: [{required: true, message: "是否展开不能为空", trigger: "blur"}]
            }) //添加表单验证规则
            const pNode = ref(null) //父节点
            const treeData = computed(() => {
                JSON.parse(JSON.stringify(data.value))
            })

            const defaultProps = reactive({
                children: "children",
                label: "menuName"
            })

            const treeRef = ref(null)
            const editFormRef = ref(null)
            const addFormRef = ref(null)


            watch(filterText, (newVal, oldVal) => {
                treeRef.value.filter(newVal);
            })

            /**
             * 加载菜单表格
             */
            const downExcel = () => {
                excel().then(res => {
                    if (res.headers['content-type'] === 'application/json') {
                        return ElMessage.error("Subject does not have permission [menu:export]");
                    }
                    const data = res.data;
                    let url = window.URL.createObjectURL(data); // 将二进制文件转化为可访问的url
                    const a = document.createElement("a");
                    document.body.appendChild(a);
                    a.href = url;
                    a.download = "菜单列表.xlsx";
                    a.click();
                    window.URL.revokeObjectURL(url);
                });
            }
            //更新菜单
            const updateMenu = () => {
                editFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        update(
                            "system/menu/update/" + editForm.value.id,
                            editForm.value
                        ).then((res) => {
                            if (res.data.success) {
                                ElNotification({
                                    type: "success",
                                    title: "成功",
                                    message: "节点信息更新",
                                });
                                editForm.value = {};
                                editlogVisible.value = false;
                                getMenuTree();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                btnLoading.value = false;
                                btnDisabled.value = false;
                                return ElMessage.error("更新菜单失败:" + res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            return ElMessage.error("更新菜单失败:" + res.data.data.errorMsg);
                        });

                    }
                });
            }
            //点击编辑节点
            const edit = (data) => {
                editTitle.value = "编辑：【" + data.menuName + "】";
                editMenu("system/menu/edit/" + data.id).then((res) => {
                    if (res.data.success) {
                        editForm.value = res.data.data;
                        editlogVisible.value = true;
                    } else {
                        return ElMessage.error("节点编辑失败:" + res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    return ElMessage.error("节点编辑失败" + res);
                });

            }
            //过滤节点
            const filterNode = (value, data) => {
                if (!value) return true;
                return data.menuName.indexOf(value) !== -1;
            }
            //关闭添加
            const addClose = () => {
                addFormRef.value.clearValidate();
                addForm.value = {};
            }
            const editClose = () => {
                editFormRef.value.clearValidate();
                editForm.value = {};
            }
            //加载菜单树
            const getMenuTree = () => {
                loading.value = true;
                tree().then((res) => {
                    if (res.data.success) {
                        data.value = res.data.data.tree;
                        open.value = res.data.data.open;
                    } else {
                        ElMessage.error("加载菜单树失败:" + res.data.data.errorMsg);
                    }
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error("加载菜单树失败:" + res);
                });
            }
            //打开添加框
            const openAdd = (data) => {
                addTitle.value = "添加节点 ：当前【" + data.menuName + "】";
                addDialogVisible.value = true;
                addForm.value.parentId = data.id;
                pNode.value = data;
            }
            //添加最高父级节点
            const openParentAdd = (data) => {
                addTitle.value = "添加第一父级";
                addDialogVisible.value = true;
                addForm.value.parentId = 0;
            }
            //点击删除按钮
            const delNode = (node, data) => {
                ElMessageBox.confirm(
                    "此操作将永久删除该节点, 是否继续?",
                    "提示",
                    {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }
                ).then((res) => {
                    if (res === "confirm") {
                        console.log(node);
                        deleteMenu(
                            "system/menu/delete/" + node.data.id
                        ).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("节点删除成功");
                                getMenuTree();
                            } else {
                                ElMessage.error("节点删除失败:" + res.data.data.errorMsg);
                            }
                        }).catch((res) => {
                            ElMessage.error("节点删除失败:" + res);
                        });
                    }
                }).catch(() => {
                    ElMessage({
                        type: "info",
                        message: "已取消删除"
                    });
                });
            }
            //发送添加节点请求
            const addNode = () => {
                addFormRef.value.validate(valid => {
                    if (!valid) {
                        return;
                    } else {
                        btnLoading.value = true;
                        btnDisabled.value = true;
                        add(addForm.value).then((res) => {
                            if (res.data.success) {
                                ElMessage.success("节点添加成功");
                                addDialogVisible.value = false;
                                getMenuTree();
                                btnLoading.value = false;
                                btnDisabled.value = false;
                            } else {
                                ElMessage.error("节点添加失败:" + res.data.data.errorMsg);
                            }
                            btnLoading.value = false;
                            btnDisabled.value = false;
                        }).catch((res) => {
                            btnLoading.value = false;
                            btnDisabled.value = false;
                            ElMessage.error("节点添加失败:" + res);
                        });

                    }
                });
            }
            // //前端添加节点
            // const append = (data, newChild) => {
            //     //   var newChild = { id: 1231, label: "qqqqq", children: [] };
            //     if (!data.children) {
            //         this.$set(data, "children", []);
            //     }
            //     data.children.push(newChild);
            // },

            // const remove = (node, data) => {
            //     const parent = node.parent;
            //     const children = parent.data.children || parent.data;
            //     const index = children.findIndex(d => d.id === data.id);
            //     children.splice(index, 1);
            // },

            const renderContent = (h, {node, data, store}) => {
                return h(
                    'span',
                    {
                        class: 'custom-tree-node',
                    },
                    h(
                        'span',
                        null,
                        h(
                            'i',
                            {
                                class: data.icon
                            },
                            "   " + node.label,
                            node.data.type == 0 ?
                                h("span",{class: "el-tag el-tag--mini el-tag--plain",style: "margin-left: 20px;"},"菜单")
                                :
                                h("span",{class: "el-tag el-tag--warning el-tag--mini el-tag--plain",style: "margin-left: 20px;"},"权限  " + node.data.perms)
                        ),
                    ),
                    node.data.menuName == "其他" ? "" :
                    h(
                        'span',
                        null,
                        h(
                            'button',
                            {
                                class: "el-button el-button--text el-button--mini",
                                onClick: () => edit(data),
                            },
                            h(
                                'i',
                                {
                                    class: "el-icon-edit",
                                },
                                ' 编辑'
                            ),
                        ),
                        h(
                            'button',
                            {
                                class: "el-button el-button--text el-button--mini",
                                onClick: () => (openAdd(data)),
                            },
                            h(
                                'i',
                                {
                                    class: "el-icon-plus",
                                },
                                ' 增加'
                            ),
                        ),
                        h(
                            'button',
                            {
                                class: "el-button el-button--text el-button--mini",
                                onClick: () => delNode(node, data),
                            },
                            h(
                                'i',
                                {
                                    class: "el-icon-delete",
                                },
                                ' 删除'
                            ),
                        ),
                    )
                )
            };

            getMenuTree();

            return {
                data,
                btnLoading,
                btnDisabled,
                loading,
                open,
                filterText,
                addDialogVisible,
                editlogVisible,
                addTitle,
                editTitle,
                addForm,
                editForm,
                addFormRules,
                pNode,
                treeData,
                defaultProps,
                treeRef,
                filterText,
                editFormRef,
                addFormRef,
                downExcel,
                updateMenu,
                edit,
                filterNode,
                addClose,
                editClose,
                getMenuTree,
                openAdd,
                openParentAdd,
                delNode,
                addNode,
                renderContent
            }
        }
    }
</script>

<style>
    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }

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

