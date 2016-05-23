/*
 * Copyright (c) 2016- syu_chan_1005
 * Released under the MIT license
 * http://opensource.org/licenses/mit-license.php
 */

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by syuchan on 2016/05/23.
 */
public class CustomRecipe implements Listener {
	public static HashSet<ItemStackRecipe> customRecipeSet = new HashSet<>();
	private static CustomRecipe instance;

	/**
	 * 初期化 使う前に一度だけ実行してください
	 * @param plugin 親プラグイン
	 */
	public static void init(Plugin plugin) {
		instance = new CustomRecipe();
		Bukkit.getPluginManager().registerEvents(instance, plugin);
	}

	/**
	 * ItemStackを使ったレシピを追加します。
	 * @param recipe Shaped, Shapeless
	 */
	public static void addRecipe(ItemStackRecipe recipe) {
		Bukkit.addRecipe(recipe.getRecipe());
		customRecipeSet.add(recipe);
	}

	private static boolean isSameItem(ItemStack item, ItemStack item2, int compareLoreLength) {
		if (item.getType().equals(item2.getType())) {
			byte data = item.getData().getData();
			byte data2 = item2.getData().getData();
			if (item.getType().getMaxDurability() != 0 || data == data2) {
				ItemMeta meta = item.getItemMeta();
				ItemMeta meta2 = item2.getItemMeta();
				return isSameDisplayName(meta, meta2) && isSameItemLore(meta, meta2, compareLoreLength);
			}
		}
		return false;
	}

	private static boolean isSameDisplayName(ItemMeta meta, ItemMeta meta2) {
		if (meta.hasDisplayName() && meta2.hasDisplayName()) {
			return meta.getDisplayName().equalsIgnoreCase(meta2.getDisplayName());
		} else if (meta.hasDisplayName() == meta2.hasDisplayName()) {
			return true;
		}
		return false;
	}

	private static boolean isSameItemLore(ItemMeta meta, ItemMeta meta2, int compareLength) {
		if (meta.hasLore() && meta2.hasLore()) {
			List<String> lore = meta.getLore();
			List<String> lore2 = meta2.getLore();
			if (lore.size() == lore2.size()) {
				for (int i = 0; i < compareLength; i++) {
					if (lore.get(i) == null) break;
					if (!lore.get(i).equalsIgnoreCase(lore2.get(i))) return false;
				}
				return true;
			}
		} else if (meta.hasLore() == meta2.hasLore()) { return true; }
		return false;
	}

	interface ItemStackRecipe {
		ItemStack getResult();
		Recipe getRecipe();
		List<ItemStackRecipeMaterial> getMaterialList();
		int getMaterialAmount();
	}

	interface ItemStackRecipeMaterial {
		ItemStack getMaterialItem();
	}

	public static class Shaped implements ItemStackRecipe {
		private ItemStack result;
		private Recipe recipe;
		private List<ItemStackRecipeMaterial> recipeMaterialList;
		private int materialAmount;

		/**
		 * 定形レシピを設定します。
		 * @param craftResultItem クラフト結果のItemStack
		 * @param recipeCodes クラフト時のグリッド指定
		 * @param recipeMaterialList クラフトに使用するItemStackMaterial
		 */
		public Shaped(ItemStack craftResultItem, String[] recipeCodes, ShapedMaterial... recipeMaterialList) {
			ShapedRecipe recipe = new ShapedRecipe(craftResultItem).shape(recipeCodes);
			for (ShapedMaterial recipeMaterial : recipeMaterialList) {
				recipe.setIngredient(recipeMaterial.getCode(), recipeMaterial.getMaterialItem().getType());
			}
			this.recipe = recipe;
			List<ItemStackRecipeMaterial> materialList = new ArrayList<>();
			materialList.addAll(Arrays.asList(recipeMaterialList));
			this.recipeMaterialList = materialList;
			this.result = craftResultItem;
			int materialAmount = 0;
			for(String recipeCode : recipeCodes) { materialAmount += recipeCode.replaceAll(" ", "").length(); }
			this.materialAmount = materialAmount;
		}

		@Override
		public ItemStack getResult() {
			return result;
		}

		@Override
		public List<ItemStackRecipeMaterial> getMaterialList() {
			return recipeMaterialList;
		}

		@Override
		public Recipe getRecipe() {
			return recipe;
		}

		@Override
		public int getMaterialAmount() {
			return materialAmount;
		}
	}

	public static class ShapedMaterial implements ItemStackRecipeMaterial {
		private ItemStack itemStack;
		private char code;

		/**
		 * 定形レシピの使用するItemStackを指定します
		 * @param itemStack 使用するItemStack
		 * @param code itemStackに対応するMaterialCode
		 */
		public ShapedMaterial(ItemStack itemStack, char code) {
			this.itemStack = itemStack;
			this.code = code;
		}

		@Override
		public ItemStack getMaterialItem() {
			return itemStack;
		}

		public char getCode() {
			return code;
		}
	}

	public static class Shapeless implements ItemStackRecipe {
		private ItemStack result;
		private Recipe recipe;
		private List<ItemStackRecipeMaterial> materialList;
		private int materialAmount;

		/**
		 * 不定形レシピを追加します。
		 * @param craftResultItem クラフト結果のItemStack
		 * @param recipeMaterialList クラフトに使用するItemStackMaterial
		 */
		public Shapeless(ItemStack craftResultItem, ShapelessMaterial... recipeMaterialList) {
			ShapelessRecipe recipe = new ShapelessRecipe(craftResultItem);
			for (ShapelessMaterial recipeMaterial : recipeMaterialList) {
				recipe.addIngredient(recipeMaterial.getAmount(), recipeMaterial.getMaterialItem().getType(), recipeMaterial.getRawData());
			}
			this.recipe = recipe;
			List<ItemStackRecipeMaterial> materialList = new ArrayList<>();
			materialList.addAll(Arrays.asList(recipeMaterialList));
			this.materialList = materialList;
			this.result = craftResultItem;
			this.materialAmount = recipeMaterialList.length;
		}

		@Override
		public ItemStack getResult() {
			return result;
		}

		@Override
		public Recipe getRecipe() {
			return recipe;
		}

		@Override
		public List<ItemStackRecipeMaterial> getMaterialList() {
			return materialList;
		}

		@Override
		public int getMaterialAmount() {
			return materialAmount;
		}
	}

	public static class ShapelessMaterial implements ItemStackRecipeMaterial {
		private ItemStack item;
		private int amount;
		private int rawData;

		/**
		 * 不定形レシピの材料指定
		 * @param material 使用するItemStack
		 */
		public ShapelessMaterial(ItemStack material) {
			this(material, 1, -1);
		}

		/**
		 * 不定形レシピの材料指定
		 * @param material 使用するItemStack
		 * @param amount 使用する個数
		 */
		public ShapelessMaterial(ItemStack material, int amount) {
			this(material, amount, -1);
		}

		/**
		 * 不定形レシピの材料指定
		 * @param material 使用するItemStack
		 * @param amount 使用する個数
		 * @param rawData ダメージ値 -1で未指定
		 */
		public ShapelessMaterial(ItemStack material, int amount, int rawData) {
			this.item = material;
			this.amount = amount;
			this.rawData = rawData;
		}

		@Override
		public ItemStack getMaterialItem() {
			return item;
		}

		public int getAmount() {
			return amount;
		}

		public int getRawData() {
			return rawData;
		}
	}

	@EventHandler
	public void ignoreDeprecatedCraft(CraftItemEvent event) {
		if(checkRecipe(event.getInventory().getContents())) {
			event.setCancelled(true);
			((Player) event.getWhoClicked()).updateInventory();
		}
	}

	@EventHandler
	public void ignoreDeprecatedRecipeDisplay(PrepareItemCraftEvent event) {
		if(checkRecipe(event.getInventory().getContents())) {
			event.getInventory().setItem(0, new ItemStack(Material.AIR));
		}
	}

	public boolean checkRecipe(ItemStack[] craftItems) {
		ItemStack resultItem = craftItems[0];
		List<ItemStackRecipe> matchedItemStackRecipe = new ArrayList<>();
		for (ItemStackRecipe ItemStackRecipe : customRecipeSet) {
			if (isSameItem(resultItem, ItemStackRecipe.getResult(), 1)) matchedItemStackRecipe.add(ItemStackRecipe);
		}
		if (matchedItemStackRecipe.isEmpty()) return false;
		for (ItemStackRecipe recipe : matchedItemStackRecipe) {
			if (isSameItem(resultItem, recipe.getResult(), 1)) {
				ArrayList<ItemStack> craftMaterials = new ArrayList<ItemStack>();
				for (int i = 1; i < craftItems.length; i++) {
					if (craftItems[i].getType() != Material.AIR) craftMaterials.add(craftItems[i]);
				}
				int matchCount = 0;
				for (ItemStackRecipeMaterial iMaterial : recipe.getMaterialList()) {
					for (ItemStack craftMaterial : craftMaterials) {
						if (isSameItem(iMaterial.getMaterialItem(), craftMaterial, 1)) matchCount++;
					}
				}
				if (matchCount == recipe.getMaterialAmount()) return false;
			}
		}
		return true;
	}
}
