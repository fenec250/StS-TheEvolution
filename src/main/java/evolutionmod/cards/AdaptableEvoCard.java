package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.orbs.AbstractGene;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class AdaptableEvoCard extends BaseEvoCard {

	protected Map<String, AbstractAdaptation> adaptationMap;
	protected boolean adaptationUpgraded = false;
	protected String initialRawDescription;
	private TooltipInfo adaptationTooltip;
	private boolean isNameAdapted;

    public AdaptableEvoCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                            final CardType type, final CardColor color,
                            final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
	    this.adaptationMap = new HashMap<>();
	    this.initialRawDescription = rawDescription;
	    this.isNameAdapted = false;
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

    public int tryAdaptingWith(AbstractOrb orb, boolean consumeOrbIfAdapted) {
		if (canAdaptWith(orb) == 0) {
			return 0;
		}
		AbstractGene gene = (AbstractGene) orb;
	    int adaptAmount = this.tryAdaptingWith(gene.getAdaptation());
	    if (consumeOrbIfAdapted && adaptAmount > 0) {
		    consumeOrb(AbstractDungeon.player, gene);
	    }
	    return adaptAmount;
    }

    public int tryAdaptingWith(AbstractAdaptation adaptation) {
	    int adaptAmount = this.canAdaptWith(adaptation);
	    if (adaptAmount > 0) {
		    addAdaptation(adaptation);
		    this.shuffleBackIntoDrawPile = true;
	    }
	    return adaptAmount;
    }

    private void addAdaptation(AbstractAdaptation adaptation) {
    	if (this.adaptationMap.containsKey(adaptation.getGeneId())) {
    		this.adaptationMap.get(adaptation.getGeneId()).amount += adaptation.amount;
    		if (this.adaptationMap.get(adaptation.getGeneId()).amount <= 0) {
			    this.adaptationMap.remove(adaptation.getGeneId());
		    }
	    } else {
    		this.adaptationMap.put(adaptation.getGeneId(), adaptation);
	    }
	    updateDescription();
    }

    protected void updateDescription() {
		int adaptationsCount = this.adaptationMap.values().stream()
				.mapToInt(abstractAdaptation -> abstractAdaptation.amount)
				.sum();
		if (this.adaptationTooltip == null) {
			this.adaptationTooltip = new TooltipInfo("Adaptations", "None");
		}
		if (adaptationsCount > 0) {
			if (!this.isNameAdapted) {
				this.name += "&";
				this.isNameAdapted = true;
			}
			String tooltip = this.adaptationMap.values().stream()
					.map(a -> {
						StringBuilder stringBuilder = new StringBuilder(a.text());
						stringBuilder.append(": ").append(a.amount);
						if (a.amount < a.max) {
							stringBuilder.append('/').append(a.max);
						}
						stringBuilder.append(" NL ");
						return stringBuilder;
					})
					.reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
					.toString();
			this.adaptationTooltip.description = colorGeneNames(tooltip);

			String adaptationDescription = " NL " + adaptationsCount +
					(adaptationsCount > 1 ? " Adaptations." : " Adaptation.");

			this.rawDescription = this.initialRawDescription + adaptationDescription;
			this.initializeDescription();
		}
    }

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		p.orbs.stream()
				.filter(o -> this.canAdaptWith(o) > 0)
				.collect(Collectors.toList())
				.forEach(o -> this.tryAdaptingWith(o, true));
		this.useAdaptations(p, m);
	}

	@Override
	public AbstractCard makeCopy() {
		AdaptableEvoCard card = (AdaptableEvoCard) super.makeCopy();
		card.adaptationMap = this.adaptationMap.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue().makeCopy()));
		card.initialRawDescription = this.initialRawDescription;
		card.isNameAdapted = this.isNameAdapted;
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
		return this.customTooltips;
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		this.shuffleBackIntoDrawPile = false;
	}

	protected void useAdaptations(AbstractPlayer p, AbstractMonster m) {
    	this.adaptationMap.values().stream()
				.filter(adaptation -> adaptation.amount > 0)
				.forEach(adaptation -> adaptation.apply(p, m));
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