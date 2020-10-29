<template>
  <div class="container">
    <header class="jumbotron">
      <h3>
        <strong>{{currentUser.username}}</strong> Profile
      </h3>
    </header>
    <p>
      <strong>Email:</strong>
      {{currentUser.email}}
    </p>
    <p>
      <strong>First name:</strong>
      {{currentUser.firstName}}
    </p>
    <p>
      <strong>Last name:</strong>
      {{currentUser.lastName}}
    </p>
    <p>
      <strong>Birthday:</strong>
      {{currentUser.birthday}}
    </p>

     <div class="form-group">
          <button class="btn btn-primary btn-block edit-btn" @click.prevent="redirectToEditForm">
            Edit
          </button>
        </div>
  </div>
</template>

<script>
export default {
  name: 'Profile',
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
  redirectToEditForm(){
  this.$router.push('/profile-edit');
  }
  }
};

</script>

<style scoped>

.edit-btn {
  width: 20%;
   float:left;
}
</style>