import {sha256} from 'js-sha256';

export const sha256Hashed = (data, salt) => {
  return sha256(salt + data);
}