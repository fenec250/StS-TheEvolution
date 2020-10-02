package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PotencyPower;
import evolutionmod.powers.RemovePotencyPower;

public class FlameStrike
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:FlameStrike";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 11;
    private static final int UPGRADE_DAMAGE_AMT = 3;
    private static final int POTENCY_AMT = 1;

    public FlameStrike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = POTENCY_AMT;
        this.isMultiDamage = true;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (AbstractGene.isPlayerInThisForm(LavafolkGene.ID)) {
            addToBot(new ApplyPowerAction(p, p, new PotencyPower(p, this.magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new RemovePotencyPower(p, this.magicNumber)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LavafolkGene()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameStrike();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}
