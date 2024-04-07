// Node v10.15.3
import axios from 'axios';
import * as CryptoJS from 'crypto-js';
import dayjs from 'dayjs';
import * as moment from 'moment';
import * as qs from 'qs';

// APP INFO
const config = {
  app_id: '2553',
  key1: 'PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL',
  key2: 'kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz',
  endpoint: 'https://sb-openapi.zalopay.vn/v2/create',
  endpoint2: 'https://sb-openapi.zalopay.vn/v2/query',
};

export const zaloPayCreateOrder = async (amount: number) => {
  const embed_data = {};

  const items = [{}];

  const transID = Math.floor(Math.random() * 1000000);
  const order: any = {
    app_id: config.app_id,
    app_trans_id: `${moment().format('YYMMDD')}_${transID}`, // translation missing: vi.docs.shared.sample_code.comments.app_trans_id
    app_user: 'user123',
    app_time: Date.now(), // miliseconds
    item: JSON.stringify(items),
    embed_data: JSON.stringify(embed_data),
    amount,
    description: `MovieApp - Payment for the order #${transID}`,
    bank_code: 'zalopayapp',
  };

  // appid|app_trans_id|appuser|amount|apptime|embeddata|item
  const data =
    config.app_id +
    '|' +
    order.app_trans_id +
    '|' +
    order.app_user +
    '|' +
    order.amount +
    '|' +
    order.app_time +
    '|' +
    order.embed_data +
    '|' +
    order.item;

  order.mac = CryptoJS.HmacSHA256(data, config.key1).toString();

  try {
    const response = await axios.post(config.endpoint, null, { params: order });
    console.log(response);
    if (response.data.return_code == 1) {
      return { zp_trans_token: response.data.zp_trans_token, order };
    }
  } catch (error) {}

  return null;
};

export const zaloPayConfirmOrder = async (app_trans_id: string) => {
  let postData: any = {
    app_id: config.app_id,
    app_trans_id,
  };

  let data = postData.app_id + '|' + postData.app_trans_id + '|' + config.key1; // appid|app_trans_id|key1
  postData.mac = CryptoJS.HmacSHA256(data, config.key1).toString();

  let postConfig = {
    method: 'post',
    url: config.endpoint2,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    data: qs.stringify(postData),
  };

  try {
    const response = await axios(postConfig);
    return response.data;
  } catch (err) {}
  return null;
};
