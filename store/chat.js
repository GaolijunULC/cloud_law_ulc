const chat = {
	state: {
		thingking: {},
		talk: null
	},
	mutations: {
		setThingking(state, id, data) {
			state.thingking[id] = data;
		},
		setTalk(state, talk) {
			state.talk = talk
		},
		talkCountPlus(state) {
			state.talk.talkCount += 2
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
		getThingking(state, id) {
			return state.thingking[id];
		},
		getTalk(state) {
			return state.talk
		}
	},
};

export default chat;