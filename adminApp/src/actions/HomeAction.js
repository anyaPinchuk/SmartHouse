import $ from 'jquery';

export function addHome(data) {
  return () => {
    return $.ajax({
      type: 'POST',
      url: '/api/house/add', data: JSON.stringify(data),
      contentType: 'application/json; charset=utf-8'
    })
  }
}
