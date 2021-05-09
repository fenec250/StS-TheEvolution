package evolutionmod.cards;

import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.ChooseAdaptationAction;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class AdaptableEvoCard extends BaseEvoCard {

	protected Map<String, AbstractAdaptation> adaptationMap;
	protected boolean adaptationUpgraded = false;
	private String initialRawDescription;
	private String cachedRawDescription;
	private TooltipInfo adaptationTooltip;

    public AdaptableEvoCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                            final CardType type, final CardColor color,
                            final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
	    this.adaptationMap = new HashMap<>();
	    this.initialRawDescription = rawDescription;
    }

    public AdaptableEvoCard(final String id, final String name, final RegionName img, final int cost, final String rawDescription,
                            final CardType type, final CardColor color,
                            final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
	    this.adaptationMap = new HashMap<>();
	    this.initialRawDescription = rawDescription;
    }

	/**
	 * Checks if this card can be adapted with the given gene
	 * @param orb
	 * @return the amount of gene of that type this card can adapt with.
	 */
	public int canAdaptWith(AbstractOrb orb) {
    	if (orb instanceof AbstractGene) {
		    return this.canAdaptWith(((AbstractGene) orb).getAdaptation());
	    }
		return 0;
    }

	protected int canAdaptWith(AbstractAdaptation adaptation) {
		String geneId = adaptation.getGeneId();
		if (!this.adaptationMap.containsKey(geneId)) {
			return 0;
		}
		int maxAmount = this.adaptationMap.get(geneId).max - this.adaptationMap.get(geneId).amount;
		if (adaptation.amount > maxAmount) {
			return maxAmount;
		}
		return adaptation.amount;
	}

    protected void updateDescription() {
		int adaptationsCount = this.adaptationMap.values().stream()
				.mapToInt(abstractAdaptation -> abstractAdaptation.amount)
				.sum();
		if (this.adaptationTooltip == null) {
			this.adaptationTooltip = new TooltipInfo("Adaptations", "None");
		}
		if (adaptationsCount > 0) {
			if (!this.rawDescription.equals(this.cachedRawDescription)) {
				this.initialRawDescription = this.rawDescription;
			}
			String tooltip = this.adaptationMap.values().stream()
					.map(a -> {
						StringBuilder stringBuilder = new StringBuilder(a.getGeneId());
						stringBuilder.append(": ").append(a.amount);
						if (a.amount < a.max) {
							stringBuilder.append('/').append(a.max);
						}
						stringBuilder.append(" NL ");
						return stringBuilder;
					})
					.reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
					.toString();
			this.adaptationTooltip.description = replaceGeneIds(tooltip);

			String adaptationDescription = " NL " + adaptationsCount +
					(adaptationsCount > 1 ? " Adaptations." : " Adaptation.");

			this.rawDescription = this.cachedRawDescription = this.initialRawDescription + adaptationDescription;
		}
		this.initializeDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
//		p.orbs.stream()
//				.filter(o -> this.canAdaptWith(o) > 0)
//				.collect(Collectors.toList())
//				.forEach(o -> this.tryAdaptingWith(o, true));
		this.adapt(99);
		this.useAdaptations(p, m);
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AdaptableEvoCard card = (AdaptableEvoCard) super.makeStatEquivalentCopy();
		card.adaptationMap = this.adaptationMap.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue().makeCopy()));
		card.initialRawDescription = this.initialRawDescription;
		card.updateDescription();
		return card;
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (this.customTooltips == null) {
			this.updateDescription();
			super.getCustomTooltips();
			this.customTooltips.add(adaptationTooltip);
		}
		if (adaptationMap.containsKey(LavafolkGene.ID) && adaptationMap.get(LavafolkGene.ID).amount > 0 && !customTooltips.contains(LavafolkGene.TOOLTIP)) customTooltips.add(LavafolkGene.TOOLTIP);
		if (adaptationMap.containsKey(ShadowGene.ID) && adaptationMap.get(ShadowGene.ID).amount > 0 && !customTooltips.contains(ShadowGene.TOOLTIP)) customTooltips.add(ShadowGene.TOOLTIP);
		if (adaptationMap.containsKey(InsectGene.ID) && adaptationMap.get(InsectGene.ID).amount > 0 && !customTooltips.contains(InsectGene.TOOLTIP)) customTooltips.add(InsectGene.TOOLTIP);
		if (adaptationMap.containsKey(HarpyGene.ID) && adaptationMap.get(HarpyGene.ID).amount > 0 && !customTooltips.contains(HarpyGene.TOOLTIP)) customTooltips.add(HarpyGene.TOOLTIP);
		if (adaptationMap.containsKey(MerfolkGene.ID) && adaptationMap.get(MerfolkGene.ID).amount > 0 && !customTooltips.contains(MerfolkGene.TOOLTIP)) customTooltips.add(MerfolkGene.TOOLTIP);
		if (adaptationMap.containsKey(CentaurGene.ID) && adaptationMap.get(CentaurGene.ID).amount > 0 && !customTooltips.contains(CentaurGene.TOOLTIP)) customTooltips.add(CentaurGene.TOOLTIP);
		if (adaptationMap.containsKey(BeastGene.ID) && adaptationMap.get(BeastGene.ID).amount > 0 && !customTooltips.contains(BeastGene.TOOLTIP)) customTooltips.add(BeastGene.TOOLTIP);
		if (adaptationMap.containsKey(PlantGene.ID) && adaptationMap.get(PlantGene.ID).amount > 0 && !customTooltips.contains(PlantGene.TOOLTIP)) customTooltips.add(PlantGene.TOOLTIP);
		if (adaptationMap.containsKey(LymeanGene.ID) && adaptationMap.get(LymeanGene.ID).amount > 0 && !customTooltips.contains(LymeanGene.TOOLTIP)) customTooltips.add(LymeanGene.TOOLTIP);
		if (adaptationMap.containsKey(SuccubusGene.ID) && adaptationMap.get(SuccubusGene.ID).amount > 0 && !customTooltips.contains(SuccubusGene.TOOLTIP)) customTooltips.add(SuccubusGene.TOOLTIP);
		if (adaptationMap.containsKey(LizardGene.ID) && adaptationMap.get(LizardGene.ID).amount > 0 && !customTooltips.contains(LizardGene.TOOLTIP)) customTooltips.add(LizardGene.TOOLTIP);
		return this.customTooltips;
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		this.shuffleBackIntoDrawPile = false;
	}

	public void adapt(int max) {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				AbstractDungeon.player.orbs.stream()
						.filter(o -> canAdaptWith(o) > 0)
						.limit(max)
						.collect(Collectors.toList())
						.forEach(o -> tryAdaptingWith(o));
				this.isDone = true;
			}
		});
	}

	public void chooseAndAdapt(int max) {
		addToBot(new ChooseAdaptationAction(max, this));
	}

	public int tryAdaptingWith(AbstractOrb orb) {
		if (canAdaptWith(orb) == 0) {
			return 0;
		}
		AbstractGene gene = (AbstractGene) orb;
		AbstractAdaptation adaptation = gene.getAdaptation();
		int adaptAmount = this.canAdaptWith(adaptation);
		if (adaptAmount > 0) {
			addAdaptation(adaptation);
			this.shuffleBackIntoDrawPile = true;
			consumeOrb(AbstractDungeon.player, gene);
		}
		return adaptAmount;
	}

	protected void addAdaptation(AbstractAdaptation adaptation) {
		GetAllInBattleInstances.get(uuid)
				.stream()
				.map(c -> (AdaptableEvoCard) c)
				.forEach(card -> {
							if (card.adaptationMap.containsKey(adaptation.getGeneId())) {
								card.adaptationMap.get(adaptation.getGeneId()).amount += adaptation.amount;
								if (card.adaptationMap.get(adaptation.getGeneId()).amount <= 0) {
									card.adaptationMap.remove(adaptation.getGeneId());
								}
							} else {
								card.adaptationMap.put(adaptation.getGeneId(), adaptation);
							}
							card.updateDescription();
						}
				);
	}

	public void useAdaptations(AbstractPlayer p, AbstractMonster m) {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				adaptationMap.values().stream()
						.filter(adaptation -> adaptation.amount > 0)
						.forEach(adaptation -> adaptation.apply(p, m));
				this.isDone = true;
			}
		});
    }

	public void useAdaptationsFast(AbstractPlayer p, AbstractMonster m) {
		addToTop(new AbstractGameAction() {
			@Override
			public void update() {
				adaptationMap.values().stream()
						.filter(adaptation -> adaptation.amount > 0)
						.forEach(adaptation -> adaptation.apply(p, m));
				this.isDone = true;
			}
		});
    }

    public abstract static class AbstractAdaptation {
    	public int amount;
    	public int max;
    	protected AbstractAdaptation(AbstractAdaptation adaptation) {
    		this(adaptation.amount, adaptation.max);
	    }
    	public AbstractAdaptation(int amount) {
    		this(amount, amount);
	    }
    	public AbstractAdaptation(int amount, int max) {
		    this.amount = amount;
    		this.max = max;
	    }
	    public abstract void apply(AbstractPlayer player, AbstractMonster monster);
	    public abstract String text();
	    public abstract String getGeneId();
		public abstract AbstractAdaptation makeCopy();
    }

    protected void upgradeAdaptationMaximum(String geneId, int change) {
    	this.adaptationMap.get(geneId).max += change;
    	this.adaptationUpgraded = true;
	}
	protected int getAdaptationMaximum() {
    	return this.adaptationMap.values().stream().map(a -> a.max).reduce(0, Integer::sum);
	}

    public static class MaxAdaptationNumber extends DynamicVariable {

        @Override
        public int baseValue(AbstractCard card) {
            return ((AdaptableEvoCard) card).getAdaptationMaximum();
        }

		@Override
		public int value(AbstractCard card) {
			return ((AdaptableEvoCard) card).getAdaptationMaximum();
		}

        @Override
        public boolean isModified(AbstractCard card) {
        	return false;
        }

        @Override
        public String key() {
            return "evolutionmod:MA";
        }

        @Override
        public boolean upgraded(AbstractCard card) {
			return ((AdaptableEvoCard) card).adaptationUpgraded;
        }
    }
}