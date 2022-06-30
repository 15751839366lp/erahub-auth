<template>
    <div id="welcome">
        <el-breadcrumb separator="/" style="padding-left:10px;padding-bottom:10px;font-size:12px;">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>欢迎</el-breadcrumb-item>
        </el-breadcrumb>
        <el-row :gutter="15">
            <!-- 左边部分 -->
            <el-col :span="13">
                <!-- 用户信息表格 -->
                <el-card class="box-card">
                    <template #header>
                        <div class="clearfix">
                            <span>用户信息</span>
                            <el-button style="float: right;" size="mini" plain loading type="primary">用户中心</el-button>
                            <el-button
                                    @click="getPage('')"
                                    type="primary"
                                    plain
                                    style="float: right;margin-right: 10px;"
                                    size="mini"
                            >获取源码
                            </el-button>
                        </div>
                    </template>
                    <el-tooltip class="item" effect="dark" content="换头像功能还未实现" placement="top-start">
                        <div class="user-avator">
                            <img src="../assets/test03.jpg"/>
                        </div>
<!--                        <el-avatar-->
<!--                                shape="square"-->
<!--                                :size="80"-->
<!--                                :src="import('../assets/test03.jpg').default"-->
<!--                                style="float:left;"-->
<!--                                :key="1"-->
<!--                        ></el-avatar>-->
                    </el-tooltip>
                    <div class="right" style="float:right;width:520px;">
                        <el-table :data="tableInfo" border height="100">
                            <el-table-column prop="username" label="用户账号"></el-table-column>
                            <el-table-column prop="realname" label="用户真实姓名"></el-table-column>
                            <el-table-column prop="department" label="所属部门"></el-table-column>
                            <el-table-column fixed="right" prop="roles" label="用户角色" width="150"></el-table-column>
                        </el-table>
                    </div>
                </el-card>
                <!-- 功能列表 -->
                <el-row style="margin-top:10px;" :gutter="10">
                    <el-card style="height:125px;">
                        <el-col :span="6">
                            <div class="grid-content bg-purple">
                                <router-link to="/business/material/product/list">
                                    <img
                                            src="../assets/1.svg"
                                            alt
                                            style="margin:0px auto; cursor: pointer;margin-left:20px;width: 60.8px;"
                                    />
                                </router-link>
                                <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资资料</div>
                            </div>
                        </el-col>
                        <el-col :span="6">
                            <div class="grid-content bg-purple-light">
                                <router-link to="/business/material/product/stocks">
                                    <img
                                            src="../assets/2.svg"
                                            alt
                                            style="cursor: pointer;margin-left:20px;width: 60.8px;"
                                    />
                                </router-link>
                                <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资库存</div>
                            </div>
                        </el-col>

                        <el-col :span="6">
                            <div class="grid-content bg-purple-light">
                                <router-link to="/business/material/product/add-stocks">
                                    <img
                                            src="../assets/3.svg"
                                            alt
                                            style="cursor: pointer;margin-left:20px;width: 60.8px;"
                                    />
                                </router-link>

                                <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资入库</div>
                            </div>
                        </el-col>
                        <el-col :span="6">
                            <div class="grid-content bg-purple"></div>
                            <router-link to="/business/material/product/publish">
                                <img
                                        src="../assets/4.svg"
                                        alt
                                        style="cursor: pointer;margin-left:20px;width: 60.8px;"
                                />
                            </router-link>
                            <div style="font-size:12px;margin-top:5px;margin-left:25px;">物资发放</div>
                        </el-col>
                    </el-card>
                </el-row>
                <el-card class="box-card" style="margin-top:20px;padding:0px;">
                    <!-- 用户登入报表 -->
                    <div id="loginReport" style="width: 650px;height:270px;"></div>
                </el-card>
            </el-col>
            <!-- 右边部分 -->
            <el-col :span="11">
                <div class="grid-content bg-purple">
                    <el-card style="min-height:650px;">
                        <el-carousel height="180px">
                            <el-carousel-item v-for="item in 3" :key="item"></el-carousel-item>
                        </el-carousel>

                        <el-divider>其他项目</el-divider>
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <el-button @click="getPage('http://116.85.25.106/backend/loginPage.do')">通用管理系统
                                    </el-button>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <el-button @click="getPage('')">社区项目</el-button>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <el-button @click="getPage('http://116.85.25.106')">商城项目</el-button>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="grid-content bg-purple">
                                    <el-button @click="getPage('')">Githhub</el-button>
                                </div>
                            </el-col>
                        </el-row>
                        <el-divider></el-divider>

                    </el-card>
                    <!-- <el-calendar :v-model="new Date()"></el-calendar> -->
                </div>
            </el-col>
        </el-row>

        <!-- <el-card class="box-card">
               <el-calendar v-model="dateValue"></el-calendar>
        </el-card>-->
    </div>
</template>
<script>
    import {ref, reactive, inject, onMounted,onBeforeUnmount} from "vue";
    import {useStore} from "vuex";
    import {ElMessage} from "element-plus";
    import {loginReport} from '../api/system/loginLog'
    import echarts from 'echarts'

    export default {
        components: {
            //别忘了引入组件
        },
        setup() {
            let xData = ref([])
            let yData = ref([])
            let myData = ref([])
            let dateValue = ref(new Date())
            let userInfo = reactive({})
            let tableInfo = ref([])
            let myChart = {}

            const store = useStore();

            /**
             * 点击获取源码
             */
            const getPage = (url) => {
                const w = window.open("about:blank");
                w.location.href = url;
            };
            /**
             * 加载登入报表数据
             */
            const getLoginReport = (username) => {
                loginReport({
                    username: username
                }).then((res) => {
                    if (!res.data.success) {
                        return ElMessage.error("获取登入报表数据失败:" + res.data.data.errorMsg);
                    } else {
                        xData.value = [];
                        yData.value = [];
                        myData.value = [];
                        res.data.data.all.forEach(e1 => {
                            xData.value.push(e1.days);
                            yData.value.push(e1.count);
                        });

                        for (let i = 0; i < xData.value.length; i++) {
                            let count = 0;
                            for (let j = 0; j < res.data.data.me.length; j++) {
                                if (xData.value[i] === res.data.data.me[j].days) {
                                    count = res.data.data.me[j].count;
                                    break;
                                } else {
                                    count = 0;
                                }
                            }
                            myData.value.push(count);
                        }
                    }
                    draw();
                }).catch((res) => {
                    return ElMessage.error("获取登入报表数据失败:" + res);
                });

            };

            /**
             * 绘制登入报表
             */
            const draw = () => {
                myChart = echarts.init(document.getElementById("loginReport"));
                // 指定图表的配置项和数据
                const option = {
                    title: {
                        text: "用户登入统计"
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            dataZoom: {
                                yAxisIndex: "none"
                            },
                            dataView: {readOnly: false}, //  缩放
                            magicType: {type: ["bar", "line"]}, ///　　折线  直方图切换
                            restore: {}, // 重置
                            saveAsImage: {} // 导出图片
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            crossStyle: {
                                color: '#eee'
                            }
                        }
                    },
                    legend: {
                        type: 'plain',
                        data: ["全部", "我"]
                    },
                    xAxis: {
                        data: xData.value
                    },
                    yAxis: {
                        type: "value"
                    },
                    series: [
                        {
                            name: "全部",
                            data: yData.value,
                            type: "bar",
                            showBackground: true,
                            animationDuration: 1500,
                            animationEasing: "cubicInOut",
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(30, 144, 255，0.5)'
                            },
                        },
                        {
                            name: "我",
                            data: myData.value,
                            type: "bar",
                            showBackground: true,
                            animationDuration: 2000,
                            animationEasing: "cubicInOut"
                        }
                    ]
                };
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }

            userInfo = store.state.login.userInfo;
            let rolesShow = null;
            if (userInfo.isAdmin) {
                rolesShow = userInfo.isAdmin ? "超级管理员" : userInfo.roles;
            } else {
                rolesShow = userInfo.roles != null && userInfo.roles.length > 1 ? userInfo.roles[0] + "  ..." : userInfo.roles;
            }

            tableInfo.value.push({
                username: userInfo.username,
                realname: userInfo.realname,
                department: userInfo.department,
                roles: rolesShow
            });

            onMounted(() => {
                getLoginReport(userInfo.username);
                draw();
            })

            onBeforeUnmount(()=> {
                myChart.dispose(); //销毁
            })

            return {
                xData,
                yData,
                myData,
                dateValue,
                userInfo,
                tableInfo,
                myChart,
                getPage,
                getLoginReport,
                draw
            };
        }
    };
</script>

<style scoped>
    .el-carousel__item h3 {
        color: #475669;
        font-size: 14px;
        opacity: 0.75;
        line-height: 200px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
        /*background-image: url("http://myforum.oss-cn-beijing.aliyuncs.com/postImages/1582867606734617c9668-8086-4c9b-867e-efda0bff78d63.png?Expires=1677475607&OSSAccessKeyId=LTAI4FsV5R1tnt8W8kqFqBYh&Signature=3xKM18EXDDD%2BQmsg%2B0t7uDC%2FMGg%3D");*/
        background-size: 100% 100%;
    }

    .el-carousel__item:nth-child(2n + 1) {
        background-color: #d3dce6;
        /*background-image: url("http://myforum.oss-cn-beijing.aliyuncs.com/postImages/15828676585536f809b01-a5c3-4229-8ce6-ed29a7bdaaa22.png?Expires=1677475658&OSSAccessKeyId=LTAI4FsV5R1tnt8W8kqFqBYh&Signature=k2fJfFzwKwp7f2c%2BRD7rdH%2FAj%2Bs%3D");*/
        background-size: 100% 100%;
    }

    .user-avator {
        float: left;
    }

    .user-avator img {
        display: block;
        width: 70px;
        height: 70px;
        border-radius: 50%;
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
