import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Copyright (c) 2016- syu_chan_1005
 * Released under the MIT license
 * http://opensource.org/licenses/mit-license.php
 */

public class ItemStackRecipe implements Listener {
	public static HashSet<IRecipe> IRecipeSet = new HashSet<>();
	private Plugin plugin;
	private static ItemStackRecipe instance;

	private ItemStackRecipe(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * 初期化 使う前に一度だけ実行してください
	 *
	 * @param plugin 親プラグイン
	 */
	public static void init(Plugin plugin) {
		if (instance == null) {
			instance = new ItemStackRecipe(plugin);
			Bukkit.getPluginManager().registerEvents(instance, instance.plugin);
		}
	}

	/**
	 * ItemStackを使ったレシピを追加します。
	 *
	 * @param recipe Shaped, Shapeless
	 */
	public static void addRecipe(IRecipe... recipe) {
		for (IRecipe iRecipe : recipe) {
			addRecipe(iRecipe);
		}
	}

	/**
	 * ItemStackを使ったレシピを追加します。
	 *
	 * @param recipe Shaped, Shapeless
	 */
	public static void addRecipe(IRecipe recipe) {
		Bukkit.addRecipe(recipe.getRecipe());
		IRecipeSet.add(recipe);
	}

	interface IRecipe {
		ItemStack getResult();

		Recipe getRecipe();

		List<IRecipeMaterial> getMaterialList();

		int getMaterialAmount();
	}

	interface IRecipeMaterial {
		ItemStack getMaterialItem();
	}

	public static class Shaped implements IRecipe {
		private ItemStack result;
		private Recipe recipe;
		private List<IRecipeMaterial> recipeMaterialList;
		private int materialAmount;

		/**
		 * 定形レシピを設定します。
		 *
		 * @param craftResultItem    クラフト結果のItemStack
		 * @param recipeCodes        クラフト時のグリッド指定
		 * @param recipeMaterialList クラフトに使用するItemStackMaterial
		 */
		public Shaped(ItemStack craftResultItem, String[] recipeCodes, ShapedMaterial... recipeMaterialList) {
			ShapedRecipe recipe = new ShapedRecipe(craftResultItem).shape(recipeCodes);
			for (ShapedMaterial recipeMaterial : recipeMaterialList) {
				recipe.setIngredient(recipeMaterial.getCode(), recipeMaterial.getMaterialItem().getType());
			}
			this.recipe = recipe;
			this.recipeMaterialList = Arrays.asList(recipeMaterialList);
			this.result = craftResultItem;
			int materialAmount = 0;
			for (String recipeCode : recipeCodes) {
				materialAmount += recipeCode.replaceAll(" ", "").length();
			}
			this.materialAmount = materialAmount;
		}

		@Override
		public ItemStack getResult() {
			return result;
		}

		@Override
		public List<IRecipeMaterial> getMaterialList() {
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

	public static class ShapedMaterial implements IRecipeMaterial {
		private ItemStack itemStack;
		private char code;

		/**
		 * 定形レシピの使用するItemStackを指定します
		 *
		 * @param itemStack 使用するItemStack
		 * @param code      itemStackに対応するMaterialCode
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

//	public static class Shapeless implements IRecipe {
//		private ItemStack result;
//		private Recipe recipe;
//		private List<IRecipeMaterial> materialList;
//		private int materialAmount;
//
//		/**
//		 * 不定形レシピを追加します。
//		 *
//		 * @param craftResultItem    クラフト結果のItemStack
//		 * @param recipeMaterialList クラフトに使用するItemStackMaterial
//		 */
//		public Shapeless(ItemStack craftResultItem, ShapelessMaterial... recipeMaterialList) {
//			ShapelessRecipe recipe = new ShapelessRecipe(craftResultItem);
//			for (ShapelessMaterial recipeMaterial : recipeMaterialList) {
//				recipe.addIngredient(recipeMaterial.getAmount(),
//						recipeMaterial.getMaterialItem().getType(),
//						recipeMaterial.getRawData());
//			}
//			this.recipe = recipe;
//			this.materialList = Arrays.asList(recipeMaterialList);
//			this.result = craftResultItem;
//			this.materialAmount = recipeMaterialList.length;
//		}
//
//		@Override
//		public ItemStack getResult() {
//			return result;
//		}
//
//		@Override
//		public Recipe getRecipe() {
//			return recipe;
//		}
//
//		@Override
//		public List<IRecipeMaterial> getMaterialList() {
//			return materialList;
//		}
//
//		@Override
//		public int getMaterialAmount() {
//			return materialAmount;
//		}
//	}
//
//	public static class ShapelessMaterial implements IRecipeMaterial {
//		private ItemStack item;
//		private int amount;
//		private int rawData;
//
//		/**
//		 * 不定形レシピの材料指定
//		 *
//		 * @param material 使用するItemStack
//		 */
//		public ShapelessMaterial(ItemStack material) {
//			this(material, 1, -1);
//		}
//
//		/**
//		 * 不定形レシピの材料指定
//		 *
//		 * @param material 使用するItemStack
//		 * @param amount   使用する個数
//		 */
//		public ShapelessMaterial(ItemStack material, int amount) {
//			this(material, amount, -1);
//		}
//
//		/**
//		 * 不定形レシピの材料指定
//		 *
//		 * @param material 使用するItemStack
//		 * @param amount   使用する個数
//		 * @param rawData  ダメージ値 -1で未指定
//		 */
//		public ShapelessMaterial(ItemStack material, int amount, int rawData) {
//			this.item = material;
//			this.amount = amount;
//			this.rawData = rawData;
//		}
//
//		@Override
//		public ItemStack getMaterialItem() {
//			return item;
//		}
//
//		public int getAmount() {
//			return amount;
//		}
//
//		public int getRawData() {
//			return rawData;
//		}
//	}

	@EventHandler
	public void onPreCraft(PrepareItemCraftEvent event) {
		IRecipe recipe = checkRecipe(event.getInventory());
		if (recipe == null) {
			event.getInventory().setResult(null);
		}
	}

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		CraftingInventory inventory = event.getInventory();
		IRecipe recipe = checkRecipe(inventory);
		if (recipe == null) {
			inventory.setResult(null);
		}
	}

	private static IRecipe checkRecipe(CraftingInventory inventory) {
		ItemStack[] craftItems = inventory.getContents();
		for (IRecipe recipe : IRecipeSet) {
			if (usedItemStack(recipe.getResult(), inventory.getResult())) {
				List<ItemStack> craftMaterials = Arrays.stream(craftItems)
						.filter(i -> i.getType() != Material.AIR)
						.collect(Collectors.toList());
				int matchCount = 0;
				for (IRecipeMaterial iMaterial : recipe.getMaterialList()) {
					for (ItemStack craftMaterial : craftMaterials) {
						if (usedItemStack(iMaterial.getMaterialItem(), craftMaterial)) matchCount++;
					}
				}
				if (matchCount == recipe.getMaterialAmount()) return recipe;
			}
		}
		return null;
	}

	private static boolean usedItemStack(ItemStack itemStack1, ItemStack itemStack2) {
		if (itemStack1 == itemStack2) return true;
		if (itemStack1 == null || itemStack2 == null) return false;
		return itemStack1.getAmount() <= itemStack2.getAmount() && itemStack1.isSimilar(itemStack2);
	}
}
