import axios from 'axios';

const API_URL = 'http://localhost:8082/api/auth/';

class AuthService {

  login(user) {
    return axios
        .post(API_URL + 'signin', {
          username: user.username,
          password: user.password
        })
        .then(response => {
          if (response.data.accessToken) {
            localStorage.setItem('user', JSON.stringify(response.data));
          }

          return response.data;
        });
  }

  logout() {
    localStorage.removeItem('user');
  }

  register(user) {
    return axios.post(API_URL + 'register', {
      username: user.username,
      email: user.email,
      password: user.password,
      firstName : user.firstName,
      lastName : user.lastName,
      birthday : user.birthday
    });
  }

  update(id, user) {

    return axios.put(API_URL + 'profile-edit/'+id, {
      username: user.username,
      email: user.email,
      password: user.password,
      firstName : user.firstName,
      lastName : user.lastName,
      birthday : user.birthday
    });
  }
}

export default new AuthService()