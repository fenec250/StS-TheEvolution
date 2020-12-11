package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.powers.GodFormPower;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntSupplier;


public abstract class BaseEvoCard extends CustomCard {

	protected List<TooltipInfo> customTooltips;
	protected String coloredRawDescription;

    public BaseEvoCard(
    		final String id, final String name, final String img, final int cost, final String rawDescription,
			final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.customTooltips = null;
//        this.coloredRawDescription = ""; This gets initialized by initializeDescription() during the superclass constructor
    }


//	@Override
//	public AbstractCard makeCopy() {
//		BaseEvoCard card = (BaseEvoCard) super.makeCopy();
//		card.adaptationMap = this.adaptationMap.entrySet().stream().collect(
//				Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue().makeCopy()));
//		return card;
//	}
	@Override
	public void initializeDescription() {
    	if (coloredRawDescription == null || !coloredRawDescription.equals(this.rawDescription)) {
			this.coloredRawDescription = colorGeneNames(this.rawDescription);
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
			if (this.rawDescription.contains(LavafolkGene.NAME)) {this.customTooltips.add(new TooltipInfo(LavafolkGene.COLOR + LavafolkGene.NAME + "[]", "Orb: " + LavafolkGene.getDescription()));}
			if (this.rawDescription.contains(ShadowGene.NAME)) {this.customTooltips.add(new TooltipInfo(ShadowGene.COLOR + ShadowGene.NAME + "[]", "Orb: " + ShadowGene.getDescription()));}
			if (this.rawDescription.contains(InsectGene.NAME)) {this.customTooltips.add(new TooltipInfo(InsectGene.COLOR + InsectGene.NAME + "[]", "Orb: " + InsectGene.getDescription()));}
			if (this.rawDescription.contains(HarpyGene.NAME)) {this.customTooltips.add(new TooltipInfo(HarpyGene.COLOR + HarpyGene.NAME + "[]", "Orb: " + HarpyGene.getDescription()));}
			if (this.rawDescription.contains(MerfolkGene.NAME)) {this.customTooltips.add(new TooltipInfo(MerfolkGene.COLOR + MerfolkGene.NAME + "[]", "Orb: " + MerfolkGene.getDescription()));}
			if (this.rawDescription.contains(CentaurGene.NAME)) {this.customTooltips.add(new TooltipInfo(CentaurGene.COLOR + CentaurGene.NAME + "[]", "Orb: " + CentaurGene.getDescription()));}
			if (this.rawDescription.contains(BeastGene.NAME)) {this.customTooltips.add(new TooltipInfo(BeastGene.COLOR + BeastGene.NAME + "[]", "Orb: " + BeastGene.getDescription()));}
			if (this.rawDescription.contains(PlantGene.NAME)) {this.customTooltips.add(new TooltipInfo(PlantGene.COLOR + PlantGene.NAME + "[]", "Orb: " + PlantGene.getDescription()));}
			if (this.rawDescription.contains(LymeanGene.NAME)) {this.customTooltips.add(new TooltipInfo(LymeanGene.COLOR + LymeanGene.NAME + "[]", "Orb: " + LymeanGene.getDescription()));}
			if (this.rawDescription.contains(SuccubusGene.NAME)) {this.customTooltips.add(new TooltipInfo(SuccubusGene.COLOR + SuccubusGene.NAME + "[]", "Orb: " + SuccubusGene.getDescription()));}
			if (this.rawDescription.contains(LizardGene.NAME)) {this.customTooltips.add(new TooltipInfo(LizardGene.COLOR + LizardGene.NAME + "[]", "Orb: " + LizardGene.getDescription()));}
		}
		return customTooltips;
	}

	public static String colorGeneNames(String text) {
    	return text
//					.replaceAll("([^\\]])" + LavafolkGene.NAME, "$1" + LavafolkGene.COLOR + LavafolkGene.NAME + "[]")
				.replaceAll(LavafolkGene.NAME, LavafolkGene.COLOR + LavafolkGene.NAME + "[]")
				.replaceAll(ShadowGene.NAME, ShadowGene.COLOR + ShadowGene.NAME + "[]")
				.replaceAll(InsectGene.NAME, InsectGene.COLOR + InsectGene.NAME + "[]")
				.replaceAll(PlantGene.NAME, PlantGene.COLOR + PlantGene.NAME + "[]")
				.replaceAll(CentaurGene.NAME, CentaurGene.COLOR + CentaurGene.NAME + "[]")
				.replaceAll(LymeanGene.NAME, LymeanGene.COLOR + LymeanGene.NAME + "[]")
				.replaceAll(LizardGene.NAME, LizardGene.COLOR + LizardGene.NAME + "[]")
				.replaceAll(HarpyGene.NAME, HarpyGene.COLOR + HarpyGene.NAME + "[]")
				.replaceAll(MerfolkGene.NAME, MerfolkGene.COLOR + MerfolkGene.NAME + "[]")
				.replaceAll(BeastGene.NAME, BeastGene.COLOR + BeastGene.NAME + "[]")
				.replaceAll(SuccubusGene.NAME, SuccubusGene.COLOR + SuccubusGene.NAME + "[]");
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

	public static boolean isPlayerInThisForm(String orbId) {
    	return GodFormPower.canBypassRequirement()
				|| AbstractDungeon.player.orbs.stream()
					.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(orbId));
	}

	public static void formEffect(String geneId, Runnable action) {
		boolean hasGene = AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(geneId));
		if (hasGene) {
			action.run();
		} else {
			AbstractGene gene = getGene(geneId);
			if (gene != null) {
				AbstractDungeon.actionManager.addToBottom(new ChannelAction(gene));
			}
			boolean bypass = GodFormPower.bypassFormRequirementOnce();
			if (bypass) {
				action.run();
			}
		}
    }

	public static boolean formEffect(String geneId) {
		boolean hasGene = AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(geneId));
		if (hasGene) {
			return true;
		} else {
			AbstractGene gene = getGene(geneId);
			if (gene != null) {
				AbstractDungeon.actionManager.addToBottom(new ChannelAction(gene));
			}
			return GodFormPower.bypassFormRequirementOnce();
		}
	}

    public static AbstractGene getGene(String geneId) {
    	switch (geneId){
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