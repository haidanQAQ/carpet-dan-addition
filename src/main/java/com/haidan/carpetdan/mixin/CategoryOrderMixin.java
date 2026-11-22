package com.haidan.carpetdan.mixin;

import carpet.api.settings.SettingsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Carpet 的 /carpet 分类列表来自 getCategories() 的 HashSet，顺序是哈希序，
 * 扩展无法通过注册顺序控制。这里把 dan 分类固定挪到列表末尾，方便找。
 */
@Mixin(SettingsManager.class)
public abstract class CategoryOrderMixin {
    @Inject(method = "getCategories", at = @At("RETURN"), cancellable = true)
    private void dan$danCategoryLast(CallbackInfoReturnable<Iterable<String>> cir) {
        Iterable<String> original = cir.getReturnValue();
        if (original == null) {
            return;
        }
        List<String> categories = new ArrayList<>();
        for (String category : original) {
            categories.add(category);
        }
        if (!categories.remove("dan")) {
            return;
        }
        categories.add("dan");
        cir.setReturnValue(categories);
    }
}
