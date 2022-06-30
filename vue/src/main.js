import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
// import utils from './api/common/utils'
// import NProgress from 'nprogress'
// import 'nprogress/nprogress.css'
// import TreeTable from 'vue-table-with-tree-grid'
// import i18n from './utils/language/i18n'

const app = createApp(App)
installElementPlus(app)


app.directive('hasPermission', (el, binding) => {
    let flag = false;//默认不显示
    let userInfo = store.state.login.userInfo;

    let value = binding.value;

    if (userInfo.isAdmin) {
        //如果是超级管理员
        flag = true;
    } else if (userInfo.perms != null && userInfo.perms != undefined && userInfo.perms.indexOf(value) != -1) {
        //如果有该权限按钮显示
        flag = true;
    }
    if (!flag) {
        // if (!el.parentNode) {
        //   el.style.display = 'none'
        // } else {
        //   el.parentNode.removeChild(el);
        // }
        el.setAttribute("disabled", true);
        el.classList.add("is-disabled");
    }
})

// app.config.globalProperties.$utils = utils

    // app.component("tree-table",TreeTable)
    // app.use(i18n)
app.use(store)
app.use(router)

app.mount('#app')



// NProgress.configure({ease: 'ease', speed: 500});
// NProgress.configure({minimum: 0.3});

