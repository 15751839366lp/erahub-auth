import vue from '@vitejs/plugin-vue'
// import vitePluginImp from 'vite-plugin-imp'
// import babel from 'vite-babel-plugin'
// import vueJsx from '@vitejs/plugin-vue-jsx'
// import createImportPlugin from 'vite-plugin-import'


export default {
    base: process.env.NODE_ENV === "production" ? "/" : process.env.VITE_APP_BASE_API,
    // base: "/",
    // publicDir: "./",
    plugins: [
        vue(),
    ],
    outDir: process.env.VITE_OUTPUT_DIR,
    optimizeDeps: {
        include: [
            'schart.js'
        ]
    },

    assetsDir: "assets",
    hostname: process.env.VITE_HOST,
    port: process.env.VITE_PORT,
    open: false,
    https: true,
    ssr: false,
    filenameHashing: false,

    //   lintOnSave：{ type:Boolean default:true } 问你是否使用eslint
    lintOnSave: true,
    productionSourceMap: false,
    // cssPreprocessOptions: {
    //     scss: {
    //         additionalData: '@import "./src/assets/scss/common.scss";' // 添加公共样式
    //     }
    // },
    build: {
        // sourcemap: false,
        brotliSize: false,
        chunkSizeWarningLimit: 2000
        // assetsPublicPath: "./",
        // rollupOptions: {
        //     output:{
        //         manualChunks(id) {
        //             if (id.includes('node_modules')) {
        //                 return id.toString().split('node_modules/')[1].split('/')[0].toString();
        //             }
        //         }
        //     }
        // }
    },
    server: {
        host: "0.0.0.0",
        port: 9999, // 端口号
        https: false, // https:{type:Boolean}
        open: false, //配置自动启动浏览器
        // 配置多个代理
        proxy: {
            "/api": {
                target: "http://127.0.0.1:8989",// 要访问的接口域名
                // ws: true,// 是否启用websockets
                changeOrigin: true, //开启代理：在本地会创建一个虚拟服务端，然后发送请求的数据，并同时接收请求的数据，这样服务端和服务端进行数据的交互就不会有跨域问题
                rewrite: (path) => path.replace(/^\/api/, '')
            }
        },
    }
}
