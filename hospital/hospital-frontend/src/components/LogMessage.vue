<template>
  <div
    class="log-message"
    :style="'background-color: ' + color"
    @mouseenter="mouseIsOver = true"
    @mouseleave="mouseIsOver = false"
  >
    <div v-if="mouseIsOver">
      {{ log.source }} -> {{ longDate }} [{{ log.type }}]
      <span style="font-size: 1.2em">{{ log.content }}</span>
    </div>

    <div v-else>
      {{ shortSource }} -> {{ shortDate }}:
      <span style="font-size: 1.2em">{{ log.content }}</span>
    </div>
  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "LogMessage",

  props: ["log"],

  data() {
    return {
      mouseIsOver: false,
      prop: {},
    };
  },

  computed: {
    color() {
      let type = this.log.type;

      if (type === "WARNING") {
        if (this.mouseIsOver) {
          return "rgba(200, 200, 0, 0.8)";
        }
        return "rgba(200, 200, 0, 0.5)";
      }

      if (type === "ERROR") {
        if (this.mouseIsOver) {
          return "rgba(255, 0, 0, 0.8)";
        }
        return "rgba(255, 0, 0, 0.5)";
      }

      if (this.mouseIsOver) {
        return "rgba(0, 0, 0, 0.5)";
      }
      return "rgba(0, 0, 0, 0.25)";
    },

    shortSource() {
      return this.log.source[0];
    },

    shortDate() {
      return moment.unix(this.log.unixSeconds).format("DD.MM kk:mm");
    },

    longDate() {
      return moment.unix(this.log.unixSeconds).format("DD.MM.YYYY kk:mm:ss");
    },
  },
};
</script>

<style scoped>
.log-message {
  border-radius: 5px;
  width: 100%;
  min-height: 35px;
  padding: 7px 7px 7px 7px;
  margin-bottom: 5px;
  background-color: ;
}
</style>
