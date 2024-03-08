import { v2 as cloudinary } from 'cloudinary';

import { config } from 'dotenv';
import { createReadStream } from 'streamifier';

config();

cloudinary.config({
  cloud_name: process.env.UPLOAD_CLOUD_NAME,
  api_key: process.env.UPLOAD_API_KEY,
  api_secret: process.env.UPLOAD_API_SECRET,
});

export const uploadFromBuffer = (file, folder = 'movie-image') => {
  return new Promise((resolve, reject) => {
    let cld_upload_stream = cloudinary.uploader.upload_stream(
      {
        folder: folder,
      },
      (error: any, result: any) => {
        if (result) {
          resolve(result);
        } else {
          reject(error);
        }
      },
    );

    createReadStream(file.buffer).pipe(cld_upload_stream);
  });
};
