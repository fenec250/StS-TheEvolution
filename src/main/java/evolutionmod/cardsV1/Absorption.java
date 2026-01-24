package evolutionmod.cardsV1;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.orbsV1.*;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.AbsorptionPower;

import java.util.List;
import java.util.stream.Collectors;

public class Absorption
		extends BaseEvoCard implements GlowingCard {
	public static final String cardID = "Absorption";
	public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Absorption.png";
	private static final int COST = 1;
	private static final int ABSORB_AMT = 1;
	private static final int UPGRADE_ABSORB_AMT = 1;

	public Absorption() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.POWER, EvolutionEnum.EVOLUTION_BLUE,
				CardRarity.RARE, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = ABSORB_AMT;
	}

	@Override
	public boolean canPlay(AbstractCard card) {
		if (card == this && AbstractDungeon.player.orbs.stream()
				.noneMatch(o -> o instanceof AbstractGene)) {
			this.cantUseMessage = EXTENDED_DESCRIPTION[0];
			return false;
		}
		return super.canPlay(card);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				List<AbstractOrb> orbs = p.orbs.stream()
						.filter(o -> o instanceof AbstractGene)
						.limit(magicNumber)
						.collect(Collectors.toList());
				consumeOrbs(p, orbs);

				orbs.forEach(o -> this.addToTop(new ApplyPowerAction(
						p, p, new AbsorptionPower(p, 1, ((AbstractGene) o)))));
				this.isDone = true;
			}
		});
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_ABSORB_AMT);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
//			this.upgradeBaseCost(UPGRADED_COST);
		}
	}

	@Override
	public int getNumberOfGlows() {
		return upgraded ? 2 : 1;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return upgraded
				? AbstractDungeon.player.orbs.stream()
				.filter(o -> o instanceof AbstractGene)
				.skip(glowIndex)
				.findFirst().isPresent()
				: AbstractDungeon.player.orbs.stream()
				.anyMatch(o -> o instanceof AbstractGene);
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		return AbstractDungeon.player.orbs.stream()
				.filter(o -> o instanceof AbstractGene)
				.skip(glowIndex).findFirst()
				.map(o -> {
					switch (o.ID) {
						case HarpyGene2.ID: return HarpyGene2.COLOR.cpy();
						case HarpyGene.ID: return HarpyGene.COLOR.cpy();
						case MerfolkGene2.ID: return MerfolkGene2.COLOR.cpy();
						case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
						case LavafolkGene2.ID: return LavafolkGene2.COLOR.cpy();
						case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
						case CentaurGene2.ID: return CentaurGene2.COLOR.cpy();
						case CentaurGene.ID: return CentaurGene.COLOR.cpy();
						case LizardGene2.ID: return LizardGene2.COLOR.cpy();
						case LizardGene.ID: return LizardGene.COLOR.cpy();
						case BeastGene2.ID: return BeastGene2.COLOR.cpy();
						case BeastGene.ID: return BeastGene.COLOR.cpy();
						case PlantGene2.ID: return PlantGene2.COLOR.cpy();
						case PlantGene.ID: return PlantGene.COLOR.cpy();
						case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
						case ShadowGene.ID: return ShadowGene.COLOR.cpy();
						case LymeanGene2.ID: return LymeanGene2.COLOR.cpy();
						case LymeanGene.ID: return LymeanGene.COLOR.cpy();
						case InsectGene2.ID: return InsectGene2.COLOR.cpy();
						case InsectGene.ID: return InsectGene.COLOR.cpy();
						case SuccubusGene2.ID: return SuccubusGene2.COLOR.cpy();
						case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
						default: return null;
					}
				}).orElse(AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy());
	}
}