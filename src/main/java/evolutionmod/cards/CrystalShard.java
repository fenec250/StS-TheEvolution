package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;

public class CrystalShard
		extends BaseEvoCard implements CustomSavable<Integer>, GlowingCard {
	public static final String ID = "evolutionmod:CrystalShard";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalShard.png";
	private static final int COST = 1;
	private static final int DAMAGE_AMT = 9;
	private static final int UPGRADE_DAMAGE_AMT = 2;
	private static final int FORM_DAMAGE = 4;
	private static final int UPGRADE_FORM_DAMAGE = 1;

	private int genesIndexes;
	private AbstractGene firstGene; // firstIndex = genesIndex / 10
	private AbstractGene secondGene; // secoIndex = genesIndex % 10

	public CrystalShard() {
		this((!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null)
				? -1
				: AbstractDungeon.miscRng.random(11 * 10 - 1));
	}

	public CrystalShard(int geneIndexes) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.damage = this.baseDamage= DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_DAMAGE;
		this.exhaust = true;
		this.genesIndexes = geneIndexes;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int damage = this.damage;

		addToBot(new DamageAction(
				m, new DamageInfo(p, damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		BaseEvoCard.formEffect(this.firstGene.ID); // effect applied in calculateBlock
		BaseEvoCard.formEffect(this.secondGene.ID, () -> addToBot(new DrawCardAction(p, 1)));
	}

	@Override
	public void applyPowers() {
		this.calculateDamage();
		super.applyPowers();
		this.baseDamage = getBaseDamage();
		this.isDamageModified = this.damage != this.baseDamage;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		this.calculateDamage();
		super.calculateCardDamage(mo);
		this.baseDamage = getBaseDamage();
		this.isDamageModified = this.damage != this.baseDamage;
	}

	public int getBaseDamage() {
		return DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
	}

	public void calculateDamage() {
		this.baseDamage = getBaseDamage();
		if (BaseEvoCard.isPlayerInThisForm(this.firstGene.ID)) {
			this.baseDamage += this.magicNumber;
		}
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public AbstractCard makeCopy() {
		return this.secondGene == null ? new CrystalShard() : new CrystalShard(this.genesIndexes);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
			this.upgradeMagicNumber(UPGRADE_FORM_DAMAGE);
		}
	}

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return firstGene != null && secondGene != null;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				if (isPlayerInThisForm(firstGene.ID)) {
					switch (firstGene.ID) {
						case HarpyGene.ID: return HarpyGene.COLOR.cpy();
						case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
						case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
						case CentaurGene.ID: return CentaurGene.COLOR.cpy();
						case LizardGene.ID: return LizardGene.COLOR.cpy();
						case BeastGene.ID: return BeastGene.COLOR.cpy();
						case PlantGene.ID: return PlantGene.COLOR.cpy();
						case ShadowGene.ID: return ShadowGene.COLOR.cpy();
						case LymeanGene.ID: return LymeanGene.COLOR.cpy();
						case InsectGene.ID: return InsectGene.COLOR.cpy();
						case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
					}
					return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
				}
			case 1:
				if (isPlayerInTheseForms(secondGene.ID, firstGene.ID)) {
					switch (secondGene.ID) {
						case HarpyGene.ID: return HarpyGene.COLOR.cpy();
						case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
						case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
						case CentaurGene.ID: return CentaurGene.COLOR.cpy();
						case LizardGene.ID: return LizardGene.COLOR.cpy();
						case BeastGene.ID: return BeastGene.COLOR.cpy();
						case PlantGene.ID: return PlantGene.COLOR.cpy();
						case ShadowGene.ID: return ShadowGene.COLOR.cpy();
						case LymeanGene.ID: return LymeanGene.COLOR.cpy();
						case InsectGene.ID: return InsectGene.COLOR.cpy();
						case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
					}
					return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
				}
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	private void resetGene() {
		if (this.genesIndexes < 0 || this.genesIndexes > 11 * 10 - 1) {
			return;
		}
		String[] validGenes = {
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
		this.firstGene =  getGene(validGenes[this.genesIndexes / 11]);
		this.secondGene = getGene(validGenes[this.genesIndexes % 10 == this.genesIndexes / 11 ? 10 : this.genesIndexes % 10]);
		this.rawDescription = EXTENDED_DESCRIPTION[0]
				+ this.firstGene.ID + EXTENDED_DESCRIPTION[1]
				+ this.secondGene.ID + EXTENDED_DESCRIPTION[2];
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.genesIndexes;
	}

	@Override
	public void onLoad(Integer integer) {
		this.genesIndexes = integer;
		resetGene();
	}
}