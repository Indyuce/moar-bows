package net.Indyuce.mb.api;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class MoarBowsUtils {
	private static String parse(MaterialData data) {
		return data.getItemType().name() + (data.getData() != 0 ? ":" + data.getData() : "");
	}

	// the array must be 9 values long
	public static String[] formatCraftingRecipe(MaterialData... craftingRecipe) {
		if (craftingRecipe.length != 9)
			return null;

		List<String> line1 = Arrays.asList(new String[] { parse(craftingRecipe[0]), parse(craftingRecipe[1]), parse(craftingRecipe[2]) });
		List<String> line2 = Arrays.asList(new String[] { parse(craftingRecipe[3]), parse(craftingRecipe[4]), parse(craftingRecipe[5]) });
		List<String> line3 = Arrays.asList(new String[] { parse(craftingRecipe[6]), parse(craftingRecipe[7]), parse(craftingRecipe[8]) });
		
		return new String[] { Strings.join(line1, ','), Strings.join(line2, ','), Strings.join(line3, ',') };
	}

	public static MoarBow getBowFromItem(ItemStack item) {
		return MoarBow.get(item);
	}
}
