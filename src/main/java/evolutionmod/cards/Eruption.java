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
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.EruptionPower;

public class Eruption
        extends CustomCard {
    public static final String ID = "evolutionmod:Eruption";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LavafolkAtt.png";
    private static final int COST = 2;
    private static final int DAMAGE_AMT = 15;
    private static final int UPGRADE_DAMAGE_AMT = 5;
    private static final int ERUPTION_AMT = 1;
    private static final int UPGRADE_ERUPTION_AMT = 1;

    public Eruption() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.isMultiDamage = true;
        this.magicNumber = this.baseMagicNumber = ERUPTION_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new EruptionPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LavafolkGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Eruption();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_ERUPTION_AMT);
        }
    }
}