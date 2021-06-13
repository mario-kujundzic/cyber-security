<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col> Logs </v-col>
            <v-col>
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Search"
                single-line
                hide-details
              ></v-text-field>
            </v-col>
          </v-card-title>

          <div class="log-panel">
            <LogMessage v-for="log in logs" :key="log.unixSeconds" :log="log"/>
          </div>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const apiURL = "api/logs";
import LogMessage from "../components/LogMessage.vue";

export default {
  name: "Logs",
  
  components: {
      LogMessage
  },

  data() {
    return {
      search: "",
      allLogs: {},
      sources: []
    };
  },

  mounted() {
    this.getLogs();
  },

  methods: {
    getLogs() {
      this.axios
        .get(apiURL)
        .then((response) => {
          this.allLogs = response.data;
        })
        .catch((error) => {
          alert(error);
        });
    },
  },

  computed: {
    logs() {
        let arr = [];

        for (let key in this.allLogs) {
            this.allLogs[key].forEach(e => {
                arr.push({
                    ...e,
                    source: key
                });
            })
        }

        arr.sort((a, b) => a.unixSeconds - b.unixSeconds);

        return arr;
    },
  },
};
</script>

<style scoped>

.log-panel {
    overflow: scroll;
    height: 500px;
    padding: 5px 5px 5px 5px;
    overflow-x: hidden;
}

</style>
