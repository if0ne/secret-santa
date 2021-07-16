{
  mode: 'production',
  resolve: {
    modules: [
      'C:\\Users\\maxik\\IdeaProjects\\secret-santa\\build\\js\\packages\\secret-santa-site\\kotlin-dce',
      'node_modules'
    ]
  },
  plugins: [
    ProgressPlugin {
      profile: false,
      handler: [Function: handler],
      modulesCount: 500,
      showEntries: false,
      showModules: true,
      showActiveModules: true
    },
    TeamCityErrorPlugin {}
  ],
  module: {
    rules: [
      {
        test: /\.js$/,
        use: [
          'source-map-loader'
        ],
        enforce: 'pre'
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader',
            options: {}
          },
          {
            loader: 'css-loader',
            options: {}
          }
        ]
      }
    ]
  },
  entry: {
    main: [
      'C:\\Users\\maxik\\IdeaProjects\\secret-santa\\build\\js\\packages\\secret-santa-site\\kotlin-dce\\secret-santa-site.js'
    ]
  },
  output: {
    path: 'C:\\Users\\maxik\\IdeaProjects\\secret-santa\\site\\build\\distributions',
    filename: [Function: filename],
    library: 'site',
    libraryTarget: 'umd',
    globalObject: 'this'
  },
  devtool: 'source-map',
  stats: {
    warningsFilter: [
      /Failed to parse source map/
    ],
    warnings: false,
    errors: false
  }
}