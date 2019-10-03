var request = require('request');

request('https://en.wikipedia.org/wiki/Morgan_Freeman', { json: true }, function (error, response, body) {
    console.log(JSON.parse(body));
//   console.log('body:', JSON.parse(body)); // Print the HTML for the Google homepage.
});