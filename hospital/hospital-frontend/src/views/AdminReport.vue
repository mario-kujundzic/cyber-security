<template>
  <v-container fill-height fluid>
      <v-row align="start" justify="center">
          <v-col>
              <v-card>
                  <v-card-title>
                    Admin Report
                  </v-card-title>

                  <v-card-text>
                      <v-text-field label=""></v-text-field>
                  </v-card-text>
              </v-card>
          </v-col>
      </v-row>
  </v-container>
</template>

<script>

export default {
  name: "AdminReport",

  data() {
    return {
        reportRequest: {
            
        }
    };
  },

  computed: {
    
  },
};
</script>

<style scoped>

</style>
