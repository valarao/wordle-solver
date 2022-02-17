const fs = require('fs');

const SOURCE_FOLDER = `${__dirname}/build`;
const TARGET_FOLDER = `${__dirname}/../resources/static`;
const SUBFOLDERS = ['', '/static', '/static/css', '/static/js'];

SUBFOLDERS.forEach((subfolder) => {
    const sourceFolderPath = `${SOURCE_FOLDER}/${subfolder}`;
    const targetFolderPath = `${TARGET_FOLDER}/${subfolder}`;

    if (!fs.existsSync(targetFolderPath)){
        fs.mkdirSync(targetFolderPath);
    }

    const files = fs.readdirSync(sourceFolderPath);
    files.forEach((file) => {
        fs.copyFile(
            `${sourceFolderPath}/${file}`,
            `${targetFolderPath}/${file}`,
            (err) => {  });
    });

});
