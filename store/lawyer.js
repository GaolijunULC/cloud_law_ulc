const lawyer = {
	state: {
		lawyer: null
	},
	mutations: {
		setLawyer(state, lawyer) {
			state.lawyer = lawyer;
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
		getLawyer(state) {
			return state.lawyer;
		}
	},
};

export default lawyer;