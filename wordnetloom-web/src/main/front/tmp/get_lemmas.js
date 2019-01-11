const fs = require('fs');


let lemmas = {
  latin: new Set(),
  yivo: new Set(),
  yiddish: new Set()
}

let processData = (err, fd) => {
  const data = JSON.parse(fd);

  data.rows.forEach(row => {
    let basicLatin = row.label.split(/(\d+)/)[0].trim();
    lemmas.latin.add(basicLatin);

    row.variants.forEach(variant => {
      for(let option of ['latin', 'yivo', 'yiddish']){
          lemmas[option].add(variant[option]);
      }
    })
  });




  console.log(JSON.stringify({
    latin: [...lemmas.latin],
    yivo: [...lemmas.yivo],
    yiddish: [...lemmas.yiddish]
  })
);

  // mergedSets = new Set([...lemmas.latin, ...lemmas.yivo, ...lemmas.yiddish])
  // console.log(JSON.stringify([...lemmas.latin, ...lemmas.yivo, ...lemmas.yiddish]));
}

fs.readFile('data.json', 'utf-8', processData);


