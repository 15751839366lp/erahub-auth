export default ({
    namespaced:true,
    state: {
        userInfo:JSON.parse(window.localStorage.getItem("userInfo")) || {},
    },
    mutations:{
        setUserInfo(state,userInfo){
            window.localStorage.setItem('userInfo', JSON.stringify(userInfo));//将传递的数据先保存到localStorage中
            state.userInfo = userInfo;// 之后才是修改state中的状态
        },
        clearUserInfo(state){
            state.userInfo = "";
        },
    },
    actions:{},
    modules: {}
})