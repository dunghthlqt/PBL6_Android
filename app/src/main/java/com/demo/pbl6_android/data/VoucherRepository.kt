package com.demo.pbl6_android.data

import com.demo.pbl6_android.data.model.Voucher
import com.demo.pbl6_android.data.model.VoucherType

object VoucherRepository {
    
    fun getShopVouchers(): List<Voucher> {
        return listOf(
            Voucher(
                id = "shop_voucher_1",
                code = "SHOP20",
                title = "Giảm 20%",
                discount = "20%",
                minAmount = 100000,
                expiryDate = "31/12/2024",
                type = VoucherType.SHOP,
                maxDiscount = 50000
            ),
            Voucher(
                id = "shop_voucher_2",
                code = "SHOP50K",
                title = "Giảm ₫50.000",
                discount = "₫50.000",
                minAmount = 200000,
                expiryDate = "30/11/2024",
                type = VoucherType.SHOP
            ),
            Voucher(
                id = "shop_voucher_3",
                code = "SHOP15",
                title = "Giảm 15%",
                discount = "15%",
                minAmount = 150000,
                expiryDate = "15/12/2024",
                type = VoucherType.SHOP,
                maxDiscount = 30000
            ),
            Voucher(
                id = "shop_voucher_4",
                code = "SHOP100K",
                title = "Giảm ₫100.000",
                discount = "₫100.000",
                minAmount = 500000,
                expiryDate = "25/12/2024",
                type = VoucherType.SHOP
            )
        )
    }
    
    fun getPlatformVouchers(): List<Voucher> {
        return listOf(
            Voucher(
                id = "platform_voucher_1",
                code = "FREESHIP",
                title = "Miễn phí vận chuyển",
                discount = "Freeship",
                minAmount = 0,
                expiryDate = "31/12/2024",
                type = VoucherType.PLATFORM
            ),
            Voucher(
                id = "platform_voucher_2",
                code = "SALE30",
                title = "Giảm 30%",
                discount = "30%",
                minAmount = 300000,
                expiryDate = "30/11/2024",
                type = VoucherType.PLATFORM,
                maxDiscount = 100000
            ),
            Voucher(
                id = "platform_voucher_3",
                code = "NEWUSER",
                title = "Giảm ₫80.000",
                discount = "₫80.000",
                minAmount = 200000,
                expiryDate = "31/12/2024",
                type = VoucherType.PLATFORM
            ),
            Voucher(
                id = "platform_voucher_4",
                code = "VIP50",
                title = "Giảm 50%",
                discount = "50%",
                minAmount = 500000,
                expiryDate = "20/12/2024",
                type = VoucherType.PLATFORM,
                maxDiscount = 200000
            ),
            Voucher(
                id = "platform_voucher_5",
                code = "MEGA200",
                title = "Giảm ₫200.000",
                discount = "₫200.000",
                minAmount = 1000000,
                expiryDate = "31/12/2024",
                type = VoucherType.PLATFORM
            )
        )
    }
}

