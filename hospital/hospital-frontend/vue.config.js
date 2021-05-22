const fs = require('fs')

module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
        https: {
          key: fs.readFileSync('./ssl/hospitalFront.key'),
          cert: fs.readFileSync('./ssl/hospitalFront.crt'),
        },
        public: 'https://localhost:8082/'
    }
}
