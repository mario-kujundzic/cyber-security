<template>
  <v-container fill-height fluid>
    <v-row align="start" justify="center">
      <v-col>
        <v-card>
          <v-card-title>
            Filters
          </v-card-title>

          <v-card-text>
            <div>
              <h4>Types</h4>
              <v-checkbox label="Info" style="margin-bottom: -20px;" v-model="showLogsType['INFO']"></v-checkbox>
              <v-checkbox label="Warning" style="margin-bottom: -20px;" v-model="showLogsType['WARNING']"></v-checkbox>
              <v-checkbox label="Error" v-model="showLogsType['ERROR']"></v-checkbox>
            </div>

            <div>
              <h4>Sources</h4>
              <v-checkbox 
                v-for="source in messageSources"
                :input-value="isSourceSelected[source]"
                :key="source"
                :label="source"
                style="margin-bottom: -20px;"
                @change="onSourceSelection($event, source)"/>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

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
            <LogMessage v-for="log in logs" :key="log.id" :log="log"/>
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
      refreshing: false,
      messageSources: [],
      isSourceSelected: {},
      selectedSources: [],
      showLogsType: {
        "INFO": true,
        "WARNING": true,
        "ERROR": true
      }
    };
  },

  mounted() {
    this.getMessageSources();
    this.refreshIfTimesUp();
    setInterval(this.refreshIfTimesUp, 1000);
  },

  methods: {
    async getMessageSources() {
      try {
        let response = await this.axios.get(apiURL + "/sources");
        this.messageSources = response.data.sources;
        this.isSourceSelected = {};
        for (let source of this.messageSources) {
          this.isSourceSelected[source] = true;
        }
      } catch (e) {
        alert(e);
      }
    },

    async getLogs() {
      // TODO: Finish this nice-to-have feature if there's time
      // let sinceUnixMilis = 0;
      // if (this.logs.length > 0) {
      //   sinceUnixMilis = this.logs[this.logs.length - 1].unixMilis
      // }

      // let queryParams = "?since=" + sinceUnixMilis;

      let queryParams = "";
      if (this.selectedSources.length > 0) {
        queryParams += "?sources=";
        for (let source of this.selectedSources) {
          queryParams += source + ","
        }
        queryParams = queryParams.slice(0, queryParams.length-1);
      }

      try {
        let response = await this.axios.get(apiURL + queryParams);
        this.allLogs = response.data;
        // TODO: Part of the nice to have feature stated earlier in this function
        // APPEND the difference to the current list
        // let newLogs = response.data;
        // for (let key in newLogs) {
        //   if (key in this.allLogs) {
        //     this.allLogs[key].push(...newLogs[key]);
        //   } else {
        //     this.allLogs[key] = newLogs[key];
        //   }
        // }
        // this.allLogs = Object.assign({}, this.allLogs);
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
    },

    async onSourceSelection(isChecked, source) {
      this.isSourceSelected[source] = isChecked;
      this.refreshSelectedSources();
      this.forceRefresh();
    },

    refreshSelectedSources() {
      this.selectedSources = [];

      for (let source in this.isSourceSelected) {
        if (this.isSourceSelected[source]) {
          this.selectedSources.push(source);
        }
      }
    },
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

        let counter = 0;
        for (let key in this.allLogs) {
            this.allLogs[key].forEach(e => {
                if (!this.showLogsType[e.type]) {
                  return;
                }

                let log = {
                    ...e,
                    source: key,
                    id: counter
                };

                if (this.search === "" || satisfiesSearch(log)) {
                  arr.push(log);
                  counter++;
                }
            });
        }

        arr.sort((a, b) => a.unixMilis - b.unixMilis);

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
