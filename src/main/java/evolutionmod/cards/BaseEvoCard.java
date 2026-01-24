package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.orbs.*;
import evolutionmod.orbsV1.*;
import evolutionmod.powers.GodlyPowersPower;
import evolutionmod.powers.MasteryPower;

import java.util.*;
import java.util.stream.Collectors;


public abstract class BaseEvoCard extends CustomCard {

	protected List<TooltipInfo> customTooltips;
	protected String coloredRawDescription;

	public static String[] GeneIds = {
			PlantGene2.ID,
			MerfolkGene2.ID,
			HarpyGene2.ID,
			LavafolkGene2.ID,
			SuccubusGene2.ID,
			LymeanGene2.ID,
			InsectGene2.ID,
			BeastGene2.ID,
			LizardGene2.ID,
			CentaurGene2.ID,
			ShadowGene2.ID};
	public static String[] GeneV1Ids = {
			PlantGene.ID,
			MerfolkGene.ID,
			HarpyGene.ID,
			LavafolkGene.ID,
			SuccubusGene.ID,
			LymeanGene.ID,
			InsectGene.ID,
			BeastGene.ID,
			LizardGene.ID,
			CentaurGene.ID,
			ShadowGene.ID};

    public BaseEvoCard(
    		final String id, final String name, final String img, final int cost, final String rawDescription,
			final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.customTooltips = null;
//        this.coloredRawDescription = ""; This gets initialized by initializeDescription() during the superclass constructor
    }

    public BaseEvoCard(
    		final String id, final String name, final RegionName img, final int cost, final String rawDescription,
			final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.customTooltips = null;
    }

	@Override
	public void initializeDescription() {
    	if (coloredRawDescription == null || !coloredRawDescription.equals(this.rawDescription)) {
			this.coloredRawDescription = replaceGeneIds(this.rawDescription);
			this.rawDescription = this.coloredRawDescription;
		}
		super.initializeDescription();
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			customTooltips = super.getCustomTooltips();
			if (customTooltips == null) {
				customTooltips = new ArrayList<>();
			}
			if (this.rawDescription.contains(LavafolkGene2.COLOR_STRING + LavafolkGene2.NAME + "[]")) {this.customTooltips.add(LavafolkGene2.TOOLTIP);}
			if (this.rawDescription.contains(ShadowGene2.COLOR_STRING + ShadowGene2.NAME + "[]")) {this.customTooltips.add(ShadowGene2.TOOLTIP);}
			if (this.rawDescription.contains(InsectGene2.COLOR_STRING + InsectGene2.NAME + "[]")) {this.customTooltips.add(InsectGene2.TOOLTIP);}
			if (this.rawDescription.contains(HarpyGene2.COLOR_STRING + HarpyGene2.NAME + "[]")) {this.customTooltips.add(HarpyGene2.TOOLTIP);}
			if (this.rawDescription.contains(MerfolkGene2.COLOR_STRING + MerfolkGene2.NAME + "[]")) {this.customTooltips.add(MerfolkGene2.TOOLTIP);}
			if (this.rawDescription.contains(CentaurGene2.COLOR_STRING + CentaurGene2.NAME + "[]")) {this.customTooltips.add(CentaurGene2.TOOLTIP);}
			if (this.rawDescription.contains(BeastGene2.COLOR_STRING + BeastGene2.NAME + "[]")) {this.customTooltips.add(BeastGene2.TOOLTIP);}
			if (this.rawDescription.contains(PlantGene2.COLOR_STRING + PlantGene2.NAME + "[]")) {this.customTooltips.add(PlantGene2.TOOLTIP);}
			if (this.rawDescription.contains(LymeanGene2.COLOR_STRING + LymeanGene2.NAME + "[]")) {this.customTooltips.add(LymeanGene2.TOOLTIP);}
			if (this.rawDescription.contains(SuccubusGene2.COLOR_STRING + SuccubusGene2.NAME + "[]")) {this.customTooltips.add(SuccubusGene2.TOOLTIP);}
			if (this.rawDescription.contains(LizardGene2.COLOR_STRING + LizardGene2.NAME + "[]")) {this.customTooltips.add(LizardGene2.TOOLTIP);}

			if (this.rawDescription.contains(LavafolkGene.COLOR_STRING + LavafolkGene.NAME + "[]")) {this.customTooltips.add(LavafolkGene.TOOLTIP);}
			if (this.rawDescription.contains(ShadowGene.COLOR_STRING + ShadowGene.NAME + "[]")) {this.customTooltips.add(ShadowGene.TOOLTIP);}
			if (this.rawDescription.contains(InsectGene.COLOR_STRING + InsectGene.NAME + "[]")) {this.customTooltips.add(InsectGene.TOOLTIP);}
			if (this.rawDescription.contains(HarpyGene.COLOR_STRING + HarpyGene.NAME + "[]")) {this.customTooltips.add(HarpyGene.TOOLTIP);}
			if (this.rawDescription.contains(MerfolkGene.COLOR_STRING + MerfolkGene.NAME + "[]")) {this.customTooltips.add(MerfolkGene.TOOLTIP);}
			if (this.rawDescription.contains(CentaurGene.COLOR_STRING + CentaurGene.NAME + "[]")) {this.customTooltips.add(CentaurGene.TOOLTIP);}
			if (this.rawDescription.contains(BeastGene.COLOR_STRING + BeastGene.NAME + "[]")) {this.customTooltips.add(BeastGene.TOOLTIP);}
			if (this.rawDescription.contains(PlantGene.COLOR_STRING + PlantGene.NAME + "[]")) {this.customTooltips.add(PlantGene.TOOLTIP);}
			if (this.rawDescription.contains(LymeanGene.COLOR_STRING + LymeanGene.NAME + "[]")) {this.customTooltips.add(LymeanGene.TOOLTIP);}
			if (this.rawDescription.contains(SuccubusGene.COLOR_STRING + SuccubusGene.NAME + "[]")) {this.customTooltips.add(SuccubusGene.TOOLTIP);}
			if (this.rawDescription.contains(LizardGene.COLOR_STRING + LizardGene.NAME + "[]")) {this.customTooltips.add(LizardGene.TOOLTIP);}
		}
		return customTooltips;
	}

	public static String replaceGeneIds(String text) {
    	return text
//					.replaceAll("([^\\]])" + LavafolkGene.NAME, "$1" + LavafolkGene.COLOR_STRING + LavafolkGene.NAME + "[]")
				.replaceAll(LavafolkGene2.ID, LavafolkGene2.COLOR_STRING + LavafolkGene2.NAME + "[]")
				.replaceAll(ShadowGene2.ID, ShadowGene2.COLOR_STRING + ShadowGene2.NAME + "[]")
				.replaceAll(InsectGene2.ID, InsectGene2.COLOR_STRING + InsectGene2.NAME + "[]")
				.replaceAll(PlantGene2.ID, PlantGene2.COLOR_STRING + PlantGene2.NAME + "[]")
				.replaceAll(CentaurGene2.ID, CentaurGene2.COLOR_STRING + CentaurGene2.NAME + "[]")
				.replaceAll(LymeanGene2.ID, LymeanGene2.COLOR_STRING + LymeanGene2.NAME + "[]")
				.replaceAll(LizardGene2.ID, LizardGene2.COLOR_STRING + LizardGene2.NAME + "[]")
				.replaceAll(HarpyGene2.ID, HarpyGene2.COLOR_STRING + HarpyGene2.NAME + "[]")
				.replaceAll(MerfolkGene2.ID, MerfolkGene2.COLOR_STRING + MerfolkGene2.NAME + "[]")
				.replaceAll(BeastGene2.ID, BeastGene2.COLOR_STRING + BeastGene2.NAME + "[]")
				.replaceAll(SuccubusGene2.ID, SuccubusGene2.COLOR_STRING + SuccubusGene2.NAME + "[]")

				.replaceAll(LavafolkGene.ID, LavafolkGene.COLOR_STRING + LavafolkGene.NAME + "[]")
				.replaceAll(ShadowGene.ID, ShadowGene.COLOR_STRING + ShadowGene.NAME + "[]")
				.replaceAll(InsectGene.ID, InsectGene.COLOR_STRING + InsectGene.NAME + "[]")
				.replaceAll(PlantGene.ID, PlantGene.COLOR_STRING + PlantGene.NAME + "[]")
				.replaceAll(CentaurGene.ID, CentaurGene.COLOR_STRING + CentaurGene.NAME + "[]")
				.replaceAll(LymeanGene.ID, LymeanGene.COLOR_STRING + LymeanGene.NAME + "[]")
				.replaceAll(LizardGene.ID, LizardGene.COLOR_STRING + LizardGene.NAME + "[]")
				.replaceAll(HarpyGene.ID, HarpyGene.COLOR_STRING + HarpyGene.NAME + "[]")
				.replaceAll(MerfolkGene.ID, MerfolkGene.COLOR_STRING + MerfolkGene.NAME + "[]")
				.replaceAll(BeastGene.ID, BeastGene.COLOR_STRING + BeastGene.NAME + "[]")
				.replaceAll(SuccubusGene.ID, SuccubusGene.COLOR_STRING + SuccubusGene.NAME + "[]");
	}

	protected static boolean shiftOrb(AbstractPlayer player, AbstractOrb orb) {
		if (player.orbs.isEmpty() || !player.orbs.contains(orb)) {
			return false;
		}
		int filledOrbCount = player.filledOrbCount();
		AbstractOrb moving = player.orbs.get(filledOrbCount -1);
		for (int i = filledOrbCount -1; i > 0 && moving != orb; --i) {
			AbstractOrb prev = player.orbs.get(i - 1);
			player.orbs.set(i-1, moving);
			moving = prev;
		}
		player.orbs.set(filledOrbCount-1, moving);
		for (int i = 0; i < player.orbs.size(); ++i) {
			player.orbs.get(i).setSlot(i, player.maxOrbs);
		}
		return true;
//	    boolean result = player.orbs.remove(orb);
//	    if (result) {
//			int orbCount = filledOrbCount;
//			if (orbCount < player.maxOrbs) {
//				AbstractOrb empty = player.orbs.get(orbCount);
//				player.orbs.add(new EmptyOrbSlot(empty.cX, empty.cY));
//
//			}
//
//			player.orbs.add(new EmptyOrbSlot(player.drawX, player.drawY));
//			player.orbs.set(orbCount, orb);
//		    for (int i = 0; i < player.orbs.size(); ++i) {
//			    player.orbs.get(i).setSlot(i, player.maxOrbs);
//		    }
//	    }
//	    return result;
	}

	protected static boolean consumeOrb(AbstractPlayer player, AbstractOrb orb) {
		if (player.orbs.isEmpty()) {
			return false;
		}
		boolean result = player.orbs.remove(orb);
		if (result) {
			player.orbs.add(new EmptyOrbSlot(player.drawX, player.drawY));
			for (int i = 0; i < player.orbs.size(); ++i) {
				player.orbs.get(i).setSlot(i, player.maxOrbs);
			}
		}
		return result;
	}

    protected static boolean consumeOrbs(AbstractPlayer player, Collection<AbstractOrb> orbs) {
	    if (player.orbs.isEmpty()) {
	    	return false;
	    }
	    boolean result = player.orbs.removeAll(orbs);
	    if (result) {
		    for (int i = 0; i < orbs.size(); ++i) {
		    	player.orbs.add(new EmptyOrbSlot(player.drawX, player.drawY));
		    }
		    for (int i = 0; i < player.orbs.size(); ++i) {
			    ((AbstractOrb)player.orbs.get(i)).setSlot(i, player.maxOrbs);
		    }
	    }
	    return result;
    }

	public static boolean isPlayerInThisForm(String orbId, String... givenOrbs) {
    	if (AbstractDungeon.player.orbs.stream()
					.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(orbId))) {
    		return true; // Orb already channeled
		}
    	if (GodlyPowersPower.canBypassRequirement(givenOrbs.length + 1)) {
    		return true; // More than enough wildcards
		}
    	if (givenOrbs.length > 0) {
			List<String> orbsList = Arrays.<String>asList(givenOrbs);
//			orbsList.add(orbId);
			List<String> playerOrbs = AbstractDungeon.player.orbs.stream()
					.filter((orb) -> orb != null && orb.ID != null)
					.map(o -> o.ID)
					.collect(Collectors.toList());
			long count = orbsList.stream()
					.filter(i -> playerOrbs.contains(i))
					.count();
			// enough wildcards to cover missing orbs
			return GodlyPowersPower.canBypassRequirement(1 + orbsList.size() - (int) count);
		}
    	return false;
	}

	public static boolean isPlayerInTheseForms(String... orbs) {
    	List orbsList = Arrays.asList(orbs);
		List<String> playerOrbs = AbstractDungeon.player.orbs.stream()
				.filter((orb) -> orb != null && orb.ID != null)
				.map(o -> o.ID)
				.collect(Collectors.toList());
		long count = orbsList.stream()
				.filter(orbId -> playerOrbs.contains(orbId))
				.count();
		return GodlyPowersPower.canBypassRequirement(orbsList.size() - (int)count);
	}

	public static void formEffect(String geneId, Runnable action) {
		boolean hasGene = AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(geneId));
		if (hasGene) {
			action.run();
			MasteryPower.formTrigger(geneId);
		} else {
			boolean bypass = GodlyPowersPower.bypassFormRequirementOnce();
			if (bypass) {
				action.run();
			}
			AbstractGene newGene = getGene(geneId);
			if (newGene != null) {
				AbstractDungeon.actionManager.addToBottom(newGene.getChannelAction());
			}
		}
    }

	public static boolean formEffect(String geneId) {
		boolean hasGene = AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(geneId));
		if (hasGene) {
			MasteryPower.formTrigger(geneId);
			return true;
		} else {
			AbstractGene gene = getGene(geneId);
			if (gene != null) {
				AbstractDungeon.actionManager.addToBottom(gene.getChannelAction());
			}
			return GodlyPowersPower.bypassFormRequirementOnce();
		}
	}

    public static AbstractGene getGene(String geneId) {
    	switch (geneId){
			case LavafolkGene2.ID: return new LavafolkGene2();
			case ShadowGene2.ID: return new ShadowGene2();
			case InsectGene2.ID: return new InsectGene2();
			case HarpyGene2.ID: return new HarpyGene2();
			case MerfolkGene2.ID: return new MerfolkGene2();
			case CentaurGene2.ID: return new CentaurGene2();
			case BeastGene2.ID: return new BeastGene2();
			case PlantGene2.ID: return new PlantGene2();
			case LymeanGene2.ID: return new LymeanGene2();
			case SuccubusGene2.ID: return new SuccubusGene2();
			case LizardGene2.ID: return new LizardGene2();

			case LavafolkGene.ID: return new LavafolkGene();
			case ShadowGene.ID: return new ShadowGene();
			case InsectGene.ID: return new InsectGene();
			case HarpyGene.ID: return new HarpyGene();
			case MerfolkGene.ID: return new MerfolkGene();
			case CentaurGene.ID: return new CentaurGene();
			case BeastGene.ID: return new BeastGene();
			case PlantGene.ID: return new PlantGene();
			case LymeanGene.ID: return new LymeanGene();
			case SuccubusGene.ID: return new SuccubusGene();
			case LizardGene.ID: return new LizardGene();
		}
    	return null;
	}
}