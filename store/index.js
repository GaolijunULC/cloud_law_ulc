import { createStore } from 'vuex';
import lawyer from './lawyer';
import user from './user'
import chat from './chat'

const store = createStore({
  modules: {
    lawyer,
	user,
	chat,
  },
});

export default store;