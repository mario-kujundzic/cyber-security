module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    // https: {
    //   key: fs.readFileSync('./ssl/server.key'),
    //   cert: fs.readFileSync('./ssl/server.crt'),
    // },
    public: 'http://localhost:8081/'
  }
};
