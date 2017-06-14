const initialState = {
  isAuthenticated: false
};


export default (state = initialState, action={}) => {
  switch (action.type) {
    case 'SET_AUTHENTICATED': {
      return {
        isAuthenticated: action.value
      }
    }
    default: {
      return state;
    }
  }
}
