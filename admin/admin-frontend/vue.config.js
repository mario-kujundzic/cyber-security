const fs = require('fs')

module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    https: {
      key: fs.readFileSync('./ssl/adminFront.key'),
      cert: fs.readFileSync('./ssl/adminFront.crt'),
    },
    public: 'https://localhost:8081/'
  }
};
