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


  // getting all values
  // console.log(JSON.stringify({
  //   latin: [...lemmas.latin],
  //   yivo: [...lemmas.yivo],
  //   yiddish: [...lemmas.yiddish]
  // });


  // getting merged

  let latinAndYivo = [];

  [...lemmas.yivo].forEach((it) => {
    latinAndYivo.push([it, 'y']);
  });

  [...lemmas.latin].forEach((it) => {
    latinAndYivo.push([it, 'l']);
  });

  // sorting alphabetically
  latinAndYivo.sort((it0, it1) => it0[0].localeCompare(it1[0]) );


  console.log(JSON.stringify({'latinAndYivo': latinAndYivo}));

  // const yivo_and_philo = new Set([...lemmas.yivo, ...lemmas.latin]);
  // console.log('joined yivo_and_philo', [...yivo_and_philo]);
  // console.log('joined len', [...yivo_and_philo].length);

// );

  // mergedSets = new Set([...lemmas.latin, ...lemmas.yivo, ...lemmas.yiddish])
  // console.log(JSON.stringify([...lemmas.latin, ...lemmas.yivo, ...lemmas.yiddish]));
}

fs.readFile('data.json', 'utf-8', processData);


