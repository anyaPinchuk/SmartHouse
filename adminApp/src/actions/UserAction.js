import $ from 'jquery';


export function login(data) {
  return () => {
    return $.ajax({
      type: 'POST',
      url: '/api/user/login',
      data: data
    });
  }
}
export function logout() {
  return dispatch => {
    return $.ajax({
      type: 'GET',
      url: '/api/logout'
    }).then(() => {
      dispatch(setAuthenticated(false));
      window.signout();
      location.href = '/login';
    });
  }
}

export function setAuthenticated(value) {
  return {
    type: 'SET_AUTHENTICATED',
    value
  }
}

export function checkRights() {
  return dispatch => {
    return $.ajax({
      type: 'GET',
      url: '/api/user/checkRights'
    }).then(res => {
      if (res.email !== '') {
        dispatch(setAuthenticated(true));
      } else {
        dispatch(setAuthenticated(false));
      }
      return res;
    });
  }
}
