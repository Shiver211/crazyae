package dev.beecube31.crazyae2.mixins;

import com.google.common.collect.ImmutableList;
import dev.beecube31.crazyae2.core.CrazyAE;
import dev.beecube31.crazyae2.common.features.Features;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.Name("CrazyAE-MixinsCore")
@Optional.Interface(iface = "zone.rong.mixinbooter.ILateMixinLoader", modid = "mixinbooter")
public class CrazyAEMixinsCore implements IFMLLoadingPlugin, ILateMixinLoader {

	public static final String MIXIN_PATH = "mixins.crazyae.%s.json";

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	@Optional.Method(modid = "mixinbooter")
	public List<String> getMixinConfigs() {
		CrazyAE.setupFeatureConfig();

		var mixins = new ArrayList<String>();

		mixins.add("mixins.crazyae.json");
		mixins.add(String.format(MIXIN_PATH, "core"));

		mixins.add(String.format(MIXIN_PATH, "priority.host"));
		mixins.add(String.format(MIXIN_PATH, "cu.combiner"));



		for (var feature : Features.values()) {
			if (!feature.isEnabled()) continue;

			var featureMixins = feature.getMixins();
			if (featureMixins != null) {
				for (var featureMixin : featureMixins) {
					mixins.add(String.format(MIXIN_PATH, featureMixin));
				}
			}

			var subFeatures = feature.getSubFeatures();
			if (subFeatures != null) {
				for (var subFeature : subFeatures) {
					if (!subFeature.isEnabled()) continue;

					var subFeatureMixins = subFeature.getMixins();
					if (subFeatureMixins != null) {
						mixins.add(String.format(MIXIN_PATH, subFeatureMixins));
					}
				}
			}
		}

		return ImmutableList.copyOf(mixins);
	}
}