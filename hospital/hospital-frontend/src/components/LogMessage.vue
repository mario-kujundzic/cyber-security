<template>
  <div
    class="log-message"
    :style="'background-color: ' + color"
    @mouseenter="mouseIsOver = true"
    @mouseleave="mouseIsOver = false"
  >
    <div>
      <span style="font-size: 0.75em; margin-right: 10px;">
        {{ timestamp }} 
      </span>
      <span :style="'color: ' + typeColor">
        <b>hospital.{{ log.source }}:</b> {{ log.content }}
      </span>
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
      mouseIsOver: false
    };
  },

  computed: {
    color() {
      if (!this.mouseIsOver) {
        return "rgba(0, 0, 0, 0)";
      }

      return "rgba(0, 0, 0, 0.05)";
    },

    typeColor() {
      let type = this.log.type;
      
      if (type === "WARNING") {
        return "darkgoldenrod";
      }

      if (type === "ERROR") {
        return "tomato";
      }

      return "black";
    },

    shortSource() {
      return this.log.source[0];
    },

    timestamp() {
      return moment.unix(this.log.unixSeconds).format("DD.MM kk:mm:ss");
    },
  },
};
</script>

<style scoped>

.log-message {
  border-radius: 5px;
  width: 100%;
  padding: 7px 7px 7px 7px;
  margin-bottom: 3px;
}

</style>
