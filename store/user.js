const user = {
	state: {
		isLogin: false,
		user: null
	},
	mutations: {
		setUser(state, user) {
			state.user = user;
		},
		login(state) {
			state.isLogin = true
		},
		logout(state) {
			state.isLogin = false
			state.user = null
		}
	},
	actions: {
		// incrementAsync({
		// 	commit
		// }) {
		// 	setTimeout(() => {
		// 		commit('setLawyer');
		// 	});
		// },
	},
	getters: {
		getUser(state) {
			return state.user;
		},
		getLoginState(state) {
			return state.isLogin;
		}
	},
};

export default user;