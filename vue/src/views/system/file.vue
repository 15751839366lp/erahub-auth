<template>
    <div class="attachments">
        <!-- 面包导航 -->
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>系统管理</el-breadcrumb-item>
            <el-breadcrumb-item>附件管理</el-breadcrumb-item>
        </el-breadcrumb>
        <!-- 图片列表部分 -->
        <el-card>
            <!-- 搜索部分 -->
            <el-form :size="'small'" :inline="true" :model="queryMap" class="demo-form-inline">
                <el-form-item label="图片路径">
                    <el-input clearable @clear="search" v-model="queryMap.path" placeholder="输入图片路径查询"></el-input>
                </el-form-item>
                <el-form-item label="图片后缀">
                    <el-select clearable @clear="search" v-model="queryMap.suffix" placeholder="请选择">
                        <el-option label="jpg/JPG" value=".jpg"></el-option>
                        <el-option label="png/PNG" value=".png"></el-option>
                        <el-option label="gif/GIF" value=".gif"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
                    <el-button type="primary" @click="centerDialogVisible=true">
                        上传
                        <i class="el-icon-upload el-icon--right"></i>
                    </el-button>
                </el-form-item>
            </el-form>
            <!-- 图片展示部分 -->
            <el-row :gutter="20" style="height:430px;" v-loading="loading">
                <el-col style="margin-top:10px;" v-for="image in this.list" :key="image.id" :span="6">
                    <div class="grid-content bg-purple">
                        <el-image
                                :alt="image.path"
                                :fit="fits"
                                :preview-src-list="srcList"
                                style="width:200px;height:170px"
                                :src="'https://www.zykhome.club/'+image.path"
                        >
                            <template #error>
                            <div class="image-slot">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                            </template>
                        </el-image>
                        <div>
                            <el-tag
                                    size="mini"
                                    effect="dark"
                                    type="success"
                                    style="margin-left:50px;"
                            >{{image.width}}px X {{image.height}}px
                            </el-tag>
                            <el-popconfirm title="这是一段内容确定删除吗？" @onConfirm="del(image.id)">
                                <template #reference>
                                <el-button
                                        v-hasPermission="'image:delete'"
                                        style="margin-left:30px;"
                                        icon="el-icon-delete"
                                        size="mini"
                                        type="text"
                                >删除
                                </el-button>
                                </template>
                            </el-popconfirm>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 分页 -->
            <el-pagination
                    style="margin-top:30px;"
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryMap.pageNum"
                    :page-sizes="[5, 10, 30, 50]"
                    :page-size="queryMap.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
            ></el-pagination>
            <!-- 上传弹出框 -->
            <el-dialog title="上传图片附件" @close="closeUpload" v-model="centerDialogVisible" width="38%" center>
        <span>
          <el-upload
                  accept="image/*"
                  :auto-upload="false"
                  :multiple="true"
                  ref="uploadRef"
                  :limit="10"
                  :on-exceed="exceed"
                  class="upload-demo"
                  drag
                  :headers="headerObject"
                  :on-success="handleUploadSuccess"
                  :action="uploadUrl"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或
              <em>点击上传</em>
            </div>
              <template #tip>
            <div class="el-upload__tip" >只能上传jpg/png文件，且不超过500kb,最多支持10张图片一起上传</div>
                  </template>
          </el-upload>
        </span>
                <template #footer>
                <span class="dialog-footer">
          <el-button @click="closeUpload" size="small">取消返回</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        </span>
                </template>
            </el-dialog>
        </el-card>
    </div>
</template>


<script>
    import {ref, reactive, computed, watch} from "vue";
    import {ElMessage, ElLoading, ElNotification, ElMessageBox} from "element-plus";
    import {getToken} from "../../utils/auth";
    import {deleteImage, findImageList} from '../../api/system/upload'

    export default {
        setup() {
            //todo
            const uploadUrl = ref(import.meta.env.DEV ? '/api/system/upload/image' : '/system/upload/image')
            const centerDialogVisible = ref(false)
            const loading = ref(true)
            const total = ref(0)
            const fits = ref("contain")
            const queryMap = ref({})
            const list = ref([])
            const srcList = ref([])
            const headerObject = reactive({
                Authorization: window.localStorage.getItem("token")
            }) //图片上传请求头

            const uploadRef = ref(null)

            /**
             * 取消上传
             */
            const closeUpload = () => {
                centerDialogVisible.value = false;
                uploadRef.value.clearFiles();
            }
            /**
             * 上传之后的回调
             */
            const handleUploadSuccess = (response, file, fileList) => {
                console.log(response)
                if (!response.success) {
                    uploadRef.value.clearFiles();
                    return ElMessage.error("上传失败:" + response.data.errorMsg);
                } else {
                    ageImageList();
                }
            }

            /**
             *
             * 点击上传到服务器
             */
            const submitUpload = () => {
                uploadRef.value.submit();
            }
            /**
             * 删除图片
             */
            const del = (id) => {
                deleteImage("system/upload/delete/" + id).then((res) => {
                    if (res.data.success) {
                        ElMessage.success("删除图片成功");
                        ageImageList();
                    } else {
                        ElMessage.error(res.data.data.errorMsg);
                    }
                }).catch((res) => {
                    ElMessage.error(res);
                })
            }
            /**
             * 加载附件列表
             */
            const ageImageList = () => {
                loading.value = true;
                findImageList(queryMap.value).then((res) => {
                    if (!res.data.success) {
                        ElMessage.error("获取附件列表失败:" + res.data.data.errorMsg);
                    } else {
                        total.value = res.data.data.total;
                        list.value = res.data.data.records;
                        srcList.value = [];
                        list.value.forEach(function (item) {
                            srcList.value.push('' + item.path);
                        });
                    }
                    loading.value = false;
                }).catch((res) => {
                    loading.value = false;
                    ElMessage.error(res);
                })
            }
            //改变页码
            const handleSizeChange = (newSize) => {
                queryMap.value.pageSize = newSize;
                ageImageList();
            }
            //翻页
            const handleCurrentChange = (current) => {
                queryMap.value.pageNum = current;
                ageImageList();
            }
            /**
             * 搜索
             */
            const search = () => {
                queryMap.value.pageNum = 1;
                ageImageList();
            }
            /**
             * 超出允许上传的时候
             */
            const exceed = () => {
                ElMessage.warning("超出允许上传图片的数量");
            }

            ageImageList();

            return {

                uploadUrl,
                centerDialogVisible,
                loading,
                total,
                fits,
                queryMap,
                list,
                srcList,
                headerObject,
                uploadRef,
                closeUpload,
                handleUploadSuccess,
                submitUpload,
                del,
                ageImageList,
                handleSizeChange,
                handleCurrentChange,
                exceed,
                search
            };
        },
    };
</script>

<style>
    .el-upload-dragger {
        width: 530px !important;

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
