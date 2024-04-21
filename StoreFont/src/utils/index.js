import slugify from "slugify";
import dayjs from "dayjs";
export const generateSlug = (title) => {
  return slugify(title, {
    replacement: "-", // replace spaces with replacement character, defaults to `-`
    remove: undefined, // remove characters that match regex, defaults to `undefined`
    lower: true, // convert to lower case, defaults to `false`
    strict: false, // strip special characters except replacement, defaults to `false`
    locale: "vi", // language code of the locale to use
    trim: true,
  });
};

export const formatDate = (date) => {
  return dayjs(date).format("DD/MM/YYYY");
};
