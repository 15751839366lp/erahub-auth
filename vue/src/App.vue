<template>
    <el-config-provider :locale="locale">
        <router-view/>
    </el-config-provider>
</template>
<script>
    import {useStore} from "vuex";
    import {defineComponent} from 'vue'
    import {ElConfigProvider} from 'element-plus'
    import zhCn from 'element-plus/lib/locale/lang/zh-cn'

    export default defineComponent({
        name: 'app',
        components: {
            ElConfigProvider,
        },
        setup() {
            const store = useStore();

            //在页面加载时读取localStorage里的状态信息
            window.localStorage.getItem("store") && store.replaceState(Object.assign(store.state, JSON.parse(window.localStorage.getItem("store"))));

            //在页面刷新时将vuex里的信息保存到localStorage里
            window.addEventListener("beforeunload", () => {
                window.localStorage.setItem("store", JSON.stringify(store.state))
            })

            return {
                locale: zhCn,
            }
        }
    });
</script>
<style>
    @import "./assets/css/main.css";
    @import "./assets/css/color-dark.css";

    html, body {
        margin: 0px;
        padding: 0px;
        width: 100%;
        height: 100%;
    }

    #app {
        width: 100%;
        height: 100% !important;
        margin: 0px;
        padding: 0px;
        position: absolute;
        background: #2d3a4b;
    }

    /* #nprogress .bar {*/
    /*      background: rgb(96, 47, 231) !important;*/
    /*}*/


</style>

