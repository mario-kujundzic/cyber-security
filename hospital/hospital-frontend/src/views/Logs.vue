<template>
  <v-container fluid>
    <v-row align="center" justify="center">
      <v-col cols="8">
        <v-card>
          <v-card-title>
            <v-col>
              Logs 
              <v-btn icon @click="forceRefresh">
                <v-icon>mdi-refresh</v-icon>
              </v-btn>
              <span v-if="refreshing" style="font-size: 0.6em;">Refreshing...</span>
              <span v-else style="font-size: 0.6em;">Refresh in {{ refreshInSeconds }}s...</span>
              <span>
                <v-text-field style="width: 120px;" type="number" min="1" label="Refresh interval (s)" v-model="refreshInterval"></v-text-field>
              </span>
            </v-col>
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
      sources: [],
      refreshInterval: 5,
      refreshInSeconds: 0,
      refreshing: false
    };
  },

  mounted() {
    this.refreshIfTimesUp();
    setInterval(this.refreshIfTimesUp, 1000);
  },

  methods: {
    async getLogs() {
      try {
        let response = await this.axios.get(apiURL);
        this.allLogs = response.data;
      } catch (e) {
        alert(e);
      }
    },

    async refreshIfTimesUp() {
      if (this.refreshing) {
        return;
      }

      this.refreshInSeconds--;
      if (this.refreshInSeconds <= 0) {
        await this.forceRefresh();
        return;
      }
    },

    async forceRefresh() {
      this.refreshing = true;
      await this.getLogs();
      this.refreshInSeconds = this.refreshInterval;
      this.refreshing = false;
    }
  },

  computed: {
    logs() {
        let self = this;
        function satisfiesSearch(log) {
          let searchTerm = self.search.toLowerCase();
          let contentMatch = log.content.toLowerCase().includes(searchTerm);
          let typeMatch = log.type.toLowerCase().includes(searchTerm);
          let sourceMatch = log.source.toLowerCase().includes(searchTerm);

          return contentMatch || typeMatch || sourceMatch;
        }

        let arr = [];

        for (let key in this.allLogs) {
            this.allLogs[key].forEach(e => {
                let log = {
                    ...e,
                    source: key
                };

                if (this.search === "" || satisfiesSearch(log)) {
                  arr.push(log);
                }

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
