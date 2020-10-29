<template>
  <div class="container">
    <header class="jumbotron">
      <h3>
        <strong>{{currentUser.username}}</strong> Profile
      </h3>
    </header>

<form name="form" @submit.prevent="handleProfileEdit">
  
          <div class="form-group">
            <label for="username">Username</label>
            <input
              v-model="currentUser.username"
              v-validate="'required|min:3|max:20'"
              type="text"
              class="form-control"
              name="username"
            />
            <div
              v-if="submitted && errors.has('username')"
              class="alert-danger"
            >{{errors.first('username')}}</div>
          </div>


          <div class="form-group">
            <label for="firtName">First name</label>
            <input
              v-model="currentUser.firtName"
              v-validate="'max:50'"
              type="text"
              class="form-control"
              name="firstName"
            />
            <div
              v-if="submitted && errors.has('firtName')"
              class="alert-danger"
            >{{errors.first('firtName')}}</div>
          </div>


          <div class="form-group">
            <label for="lastName">Last name</label>
            <input
              v-model="currentUser.lastName"
              v-validate="'max:50'"
              type="text"
              class="form-control"
              name="lastName"
            />
            <div
              v-if="submitted && errors.has('lastName')"
              class="alert-danger"
            >{{errors.first('lastName')}}</div>
          </div>


          <div class="form-group">
            <label for="birthday">Birthday (yyyy-mm-dd)</label>
            <input
              v-model="currentUser.birthday"
              type="text"
              class="form-control"
              name="birthday"
            />
            <div
              v-if="submitted && errors.has('birthday')"
              class="alert-danger"
            >{{errors.first('birthday')}}</div>
          </div>


          <div class="form-group">
            <label for="email">Email</label>
            <input
              v-model="currentUser.email"
              v-validate="'required|email|max:50'"
              type="email"
              class="form-control"
              name="email"
            />
            <div
              v-if="submitted && errors.has('email')"
              class="alert-danger"
            >{{errors.first('email')}}</div>
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input
              v-model="currentUser.password"
              v-validate="'required|min:6|max:40'"
              type="password"
              class="form-control"
              name="password"
            />
            <div
              v-if="submitted && errors.has('password')"
              class="alert-danger"
            >{{errors.first('password')}}</div>
          </div>

          <div class="form-group">
            <button class="btn btn-primary btn-block">Update</button>
          </div>
      </form>

           <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >{{message}}</div>
  </div>
</template>

<script>
export default {
  name: 'ProfileEdit',
  data() {
    return {
      submitted: false,
      successful: false,
      message: ''
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
   if (!this.currentUser) {
      this.$router.push('/login');
    }
  },
  methods: {
    handleProfileEdit() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then(isValid => {
        if (isValid) {
          this.$store.dispatch('auth/profile-edit', this.currentUser).then(
            data => {
              this.message = data.message;
              this.successful = true;
            },
            error => {
              this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
              this.successful = false;
            }
          );
        }
      });
    }
  }
};
</script>
