export enum OrderType {
  PremiumUpgrade = 'premium_upgrade',
  ProductPurchase = 'product_purchase',
  Subscription = 'subscription',
}

export const ORDER_AMOUNT = {
  premium_upgrade: 99000,
  product_purchase: 0,
  subscriptionSubscription: 0,
};

export enum OrderStatus {
  Pending = 'pending',
  Completed = 'completed',
  Canceled = 'canceled',
}
